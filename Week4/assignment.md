# 4주차 과제

<br/>

## 목차

- 과제 전 개념 정리
- Spring 환경 구축

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
3. Nginx와 tomcat 연동하기
4. Spring-boot 와 RDS 연결하기 

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

tomcat을 사용하기 위해서는 nginx에 오는 요청을 tomcat으로 보내줘야 한다. 이를 위해서는 nginx의 server 블록에서 proxy를 설정해 주어야 한다. 

<br/>

- tomcat?

tomcat은 web server에서 처리하지 못하는 동적파일을 처리해준다. 

- Proxy?

proxy는 대신이라는 의미로 중계자 역할을 한다. 예를 들어 nginx에 요청이 들어오면 proxy server가 해당 요청을 tomcat으로 보내준다. 

<br/>

<br/>

4.

spring boot template 에서 database에 접근하기 위해서는 aplication.yml 파일에서 datasource 부분을 내가 원하는 연결하고자 하는 database로 설정해 줘야 한다. 

<hr/>

