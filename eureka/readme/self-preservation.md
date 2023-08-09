
# Self-Preservation

> EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE

[D2 - 동영상 플랫폼 개선기](https://d2.naver.com/helloworld/3963776)

## 기본 헬스 동작

Eureka 인스턴스는 leaseRenewalIntervalInSeconds 설정(기본값: 30초)에 의해 주기적으로 Eureka 서버에 자신의 상태를 알려준다(renew).  
Eureka 인스턴스의 leaseExpirationDurationInSeconds 설정에 지정된 시간(기본값: 90초) 동안 Eureka 인스턴스에서 renew 요청이 오지 않으면 Eureka 서버는 해당 Eureka 인스턴스가 작동하지 않는다고 판단하고 이를 제외한다.  
Eureka 클라이언트는 registryFetchIntervalSeconds 설정에 지정된 시간(기본값: 30초)마다 서버에서 Registry 정보를 받아와서 로컬 캐시에 저장해 사용한다.  
Registry 정보의 변경 사항은 instanceInfoReplicationIntervalSeconds 설정(기본값: 30초)에 의해 주기적으로 복제된다(replication).  
Eureka 서버는 애플리케이션 시작 시 다른 Eureka 서버와 Registry 정보를 동기화한다. 그 외의 동작에서는 Eureka 클라이언트의 설정과 작동 방식을 따른다.  

## [preservation 동작](https://github.com/Netflix/eureka/wiki/Server-Self-Preservation-Mode)

Eureka 서버는 enableSelfPreservation 설정을 통해 self preservation 모드를 제어할 수 있다(기본값: true).  
이 설정은 네트워크 문제로 Eureka 클라이언트가 대규모로 누락되는 사태를 방지하기 위한 설정이다.  
Eureka 서버가 Eureka 클라이언트에서 받아야 하는 renew 요청이 특정 임곗값(기본값: 15분 내 받아야 하는 renew 요청에 대한 기댓값의 85%) 이하가 되면 self preservation 모드가 작동한다.  
이때 Eureka 클라이언트에서 더 이상 renew 요청이 없어도 Registry 정보에서 해당 클라이언트를 제외하지 않고 보존한다.  

--- 

[Baeldung - Guide to Eureka Self Preservation and Renewal](https://www.baeldung.com/eureka-self-preservation-renewal)

[subji - Eureka self-preservation](https://subji.github.io/posts/2020/08/11/springcloudeurekaregistry)

## Self-Preservation 설정

- eureka.instance.lease-renewal-interval-in-seconds = 30

Eureka Client 가 여전히 활성 상태임을 나타내기 위해 Eureka Server 에 heartbeat 를 보내는 빈도를 나타내며 기본값은 30초이다.  
즉, 30초 마다 한번씩 heartbeat 를 수신하게 된다. 가급적 기본값인 30초 사용을 권장한다.

- eureka.instance.lease-expiration-duration-in-seconds = 90

Server 레지스트리에서 Client Instance 를 제거하기 전에 마지막 heartbeat 를 받은 후 서버가 대기하는 시간을 나타낸다.  
이값은 앞서 말한 lease-renewal-interval-in-seconds 보다 커야한다.  
이 값을 너무 길게 설정하면 레지스트리의 활성 상태가 설정값에 따라 달라지므로 이전에 설명한 lease-renewal-interval-in-seconds 의 분당 실제 heartbeat 계산의 정밀도에 영향을 주게 된다고 한다.  
또한 너무 작게 설정하면 시스템이 일시적인 네트워크 결함을 견딜 수 없게 된다고 한다.  

- eureka.server.eviction-interval-timer-in-ms = 60 * 1000

이 설정은 Instance 가 종료되고 만료가 되었을때 레지스트리에서 Instance 를 제거하는 스케쥴러가 이 빈도로 실행된다.  
lease-expiration-duration-in-seconds 의 값을 너무 길게 설정하면 시스템이 자체 Self-Preservation 으로 들어가는 것이 지연된다.  

- eureka.server.renewal-percent-threshold = 0.85

이 값은 분당 예상 heartbeat 를 계산하는 데 사용된다.  
이값의 계산은 Eureka 대시보드를 보면 알 수 있는데, Renews Threshold 와 Renews (last min) 부분을 보면 알 수 있다.  
첫번째는 갱신 기준이 되는 임계값이며, 두번째는 마지막 갱신된 값이다. 즉 “EMERGENCY! EUREKA MAY BE BLA BLA SAFE.” 와 같은 에러가 나면서 Instance 가 종료되지 않는다면 Renews Threshold 값이 Renews (last min) 보다 클 경우에 발생하는 에러이다.  
**임계 값이 더 클 경우 Self-Preservation 모드가 실행되며 만료된 Instance 가 종료되지 않기 때문이다.**  
**Self-Preservation 을 발동시키면 renewal rate가 기대한 임계값만큼 올라올때 까지 instance 퇴출작업을 멈춘다.**
 
- eureka.server.renewal-threshold-update-interval-ms = 15 * 60 * 1000

레지스트리에서 Instance 를 제거하는 스케줄러는 분당 예상 heartbaet 를 계산하는 것이 여기서 설정된 빈도 값 만큼 실행된다.

- eureka.server.enable-self-preservation = true

Self-Preservation 모드를 켜고 끌 수 있는 설정이다. 가급적이면 운영 상황에서는 끄지 않는 것을 권장한다.

## Renewal-percent-threshold 계산

- Renews Threshold 

분당 heartbeat 수 (기본값인 30초에 한번으로 가정) * Instance 수 + Spring 에서 구성하는 최소 Instance 수 (1) * renewal-percent-threshold 값 계산 후 반올림을 한다.
ex: 2 * 1 + 1 * 0.85 = 3

- Renews (last min)

분당 heartbeat 수 (기본값인 30초에 한번으로 가정) * Instance 수
ex: 2 * 1 = 2
