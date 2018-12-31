Spring Boot Batch[처음으로 배우는 스프링 부트 2 - 김영재]
----
1. 배치란?
    + 프로그램의 흐름에 따라 순차적으로 자료를 처리한다는 뜻으로, 배치 처리는 '일괄 처리'와 같은 말
2. 배치 처리에 스프링 부트 배치를 써야 하는 이유?
    + **대용량 데이터 처리**에 최적화되어 고성능을 발휘
    + 효과적인 로깅, 통계 처리, 트랜잭션 관리 등 **재사용 가능한 필수 기능**을 지원
    + 수동으로 처리하지 않도록 **자동화**되어 있다.
    + 예외사항과 비정상 동작에 대한 방어 기능이 있다.
    + 스프링 부트 배치의 반복되는 작업 프로세스를 이해하면, 비즈니스 로직에 집중이 가능하다.

3. 스프링 부트 배치 주의사항
    + 가능하면 단순화해서 복잡한 구조와 로직을 피하자.
    + 데이터를 직접 사용하는 작업이 빈번하게 일어나므로, 데이터 무결성을 유지하는 유효성 *검사 겸사 등의 방어책*이 있어야 한다.
    + 배치 처리 시, 시스템 I/O 사용을 최소화해야 한다.(잦은 I/O로 데이터베이스 커넥션과 네트워크 비용이 커지면 성능에 영향을 줄 수 있다.)
        따라서 가능하면 *한번에 데이터를 조회하여 메모리에 저장*해두고 처리를 한 다음, 그 결과를 한번에 데이터베이스에 저장하는 것이 좋다.
    + 일반적으로 같은 서비스에 사용되는 웹, API, 배치, 기타 프로젝트들은 서로 영향을 줍니다.
        따라서 배치 처리가 진행되는 동안 다른 프로젝트 요소에 영향을 주는 경우가 없는지 주의를 기울여야 합니다.
    + *스프링 부트 배치는 스케줄러를 제공하지 않습니다.* (스프링에서 제공하는 쿼츠 프레임 워크, IBM 티볼리 스케줄러, BMC 컨트롤-M 등을 이용하자).
        단, 리눅스 crontab 명령은 가장 간단히 사용할 수 있지만 이는 추천하지 않는다.
            why?) 각 서버마다 따로 스케줄링을 관리해야 하며, 무엇보다 클러스트링 기능이 제공되지 않기 때문이다.
                    반면에 쿼츠와 같은 스케줄링 프레임워크를 사용한다면, 클러스터링뿐만 아니라 다양한 스케줄링 기능, 실행 이력관리 등 여러 이점이 있다.
4. 스프링 부트 배치의 일반적인 처리 절차
    1. 읽기 : 데이터 저장소(일반적으로 데이터베이스)에서 특정 데이터 레코드를 읽습니다.
    2. 처리 : 원하는 방식으로 데이터를 가공/처리합니다.
    3. 쓰기 : 수정된 데이터를 다시 저장소에 저장합니다.