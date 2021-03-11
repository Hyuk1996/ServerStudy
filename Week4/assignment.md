

# 4주차 과제

<br/>

## 목차

- 과제 전 개념 정리
- Spring 환경 구축
- API 작성

<hr/>

<br/>

## 과제 전 개념 정리

- 라이브러리(library)?

재사용이 필요한 기능으로 반복적인 코드 작성을 없애기 위해 언제든지 필요한 곳에서 호출하여 사용할 수 있도록 Class나 Function으로 만들어진 것이다. 

[출처](https://blog.gaerae.com/2016/11/what-is-library-and-framework-and-architecture-and-platform.html)

- 프레임워크(framework)?

framework의 사전적 의미는 '프로그래밍에서 특정 운영 체제를 위한 응용 프로그램 표준 구조를 구현하는 클래수와 라이브러리 모임' 이다. 이를 좀더 쉽게 표현하자면 각 소프트웨어마다 일종의 규칙이 있고, 그 규칙을 정하는 일이다. 

예를 들어, A 쇼핑몰은 보안이 중요한 회사라고 하자. 그러면 A 쇼핑몰의 프레임워크는 그 회사만의 보안에 특화된 프레임워크 일것이다. 

[출처](https://www.castingn.com/sourcing/kkultip_detail/110)

- 템플릿(template)?

템플릿은 단어 그대로 틀을 의미한다. 

- Template vs framework

템플릿은 보통 어느 개발자가 자기 입맞에 맞춰서 코드를 짜둔것이다. 이때 라이브러리 호출도 해둘 수 있고 전반적인 코드는 완성에 가까운 상태이다.

프레임워크는 라이브러리와 전체적인 로직을 제공해준다. 따라서 개발자는 로직에 따라 원하는 라이브러리를 호출해서 코드를 완성해 간다. 

- Spring-boot?

Spring-boot은 java로 어플리케이션을 쉽고, 빠르고, 간편하게 개발할 수 있게 각종 라이브러리를 모아둔 도구이다. 즉 framework이다. 

[출처](https://abc1211.tistory.com/639)

<hr/>

<br/>

## Spring 환경 구축

<br/>

### 과정

1. JDK 설치
2. Spring-boot template 가져오기
3. Intellij 이용해서 ec2에 접속하기
4. Nginx와 tomcat 연동하기
5. Spring-boot 와 RDS 연결하기 
6. 환경 Test 

<br/>

1.

Spring은 자바기반 framework이므로 ubuntu 18.04에 JDK(java development kit)을 설치해야 한다. 

```bash
sudo apt install default-jdk -y
```

위 명령어로 JDK 설치를 진행한다. 

<br/>

<br/>

2.

이제 Spring-boot 을 이용해 만든 template을 내 aws ec2 instance에 가져와야 한다. 이 과정은 cyberduck을 이용해 해당 template을 ec2 instance에 복사해주면 된다. 

<br/>

<br/>

3.

IntelliJ에서 SSH로 aws ec2 instance에 연결하면 intellij에서 아주 편하게 작업할 수 있다. 

연결 방법은 일단 프로잭트를 만든 뒤, tool -> deployment -> configure에 SFTP로 ec2에 연결설정을 해준다. 

그러면 이제 intellij에서 SFTP로 ec2에 접근할 수 있다. 그리고 tool -> start SSH session을 누르면 IntelliJ에서 terminal 환경으로 ec2에 접근할 수 있다. 

<br/>

<br/>

4.

tomcat을 사용하기 위해서는 nginx에 오는 요청을 tomcat으로 보내줘야 한다. 이를 위해서는 nginx의 server 블록에서 proxy를 설정해 주어야 한다. 

<br/>

- tomcat?

tomcat은 servlet container이다. 

- servlet container ?

servlet container는 servlet을 관리한다.

- Servlet ?

Servlet은 클라이언트의 요청을 받아 처리하고 그 결과를 클라이언트에게 반환해주는 Servlet class의 구현 규칙을 지킨 자바 프로그램이다.  

- Proxy?

proxy는 대신이라는 의미로 중계자 역할을 한다. 예를 들어 nginx에 요청이 들어오면 proxy server가 해당 요청을 tomcat으로 보내준다. 

<br/>

<br/>

5.

spring boot template 에서 database에 접근하기 위해서는 aplication.yml 파일에서 datasource 부분을 내가 원하는 연결하고자 하는 database로 설정해 줘야 한다. 

<br/>

- Url(Uniform Resource Locator)?

URL은 웹에서 정해진 유일한 자원의 주소이다. 즉 client는 url을 통해 서버의 자원에 접근한다. 

- url의 구조

https://www.hyukserver.site:80/test

https - 프로토콜 규악, 즉 브라우저가 어떤 프로토콜을 이용해서 자원에 접근할지를 나태난다.

www.hyukserver.site - 접속할 서버의 domain이다. 

80 - 서버의 80번 포트에게 요청한다는 의미이다. 

/test. ~ - 서버에서 접근하고자 하는 자원에 대한 경로이다. 

<br/>

<br/>

6.

Postman을 이용해 aws ec2 instance 주소에 request 을 보내서 제대로된 결과 값이 return 되면 잘 연결된 것이다. 

<hr/>

<br/>

## API 작성

API를 작성하기 위해서는 spring-boot template의 구조를 알아야한다. 자세한 내용은 api-server-spring-boot-final의 README 파일을 보면 알 수 있다. 

<br/>

- spring-boot template 기본 구조

Controller - Request를 받아 route를 service/provider에게 전해준다.

Service/Provider - Controller가 준 요청을 받아서 Dao로 넘겨준다. 이때 Service는 update 관련된 작업을 담당하고, Provider는 select 관련 작업을 담당한다.

Dao - 실질적으로 DB에서 부터 query를 통해 원하는 data를 가져온다. 

- Annotation?

Annotation의 사전적 의미는 '주석'이라는 의미를 가지고 있다. 따라서 자바 코드에 주석처럼 달려 특수한 의미를 부여해준다. 