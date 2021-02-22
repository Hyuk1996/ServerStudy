# 수업 내용 정리

## packet

client와 server가 주고받는 data의 단위를 packet이라고 한다.

packet은 header와 body로 이루어져 있다. 그리고 header에는 metadata, body에는 data가 들어있다. 

<br/>

## HTTP Method

- HTTP Method?

http method는 정의적으로 해석하면 '클라이언트와 서버 사이에 이루어지는 요청과 응답 데이터를 전송하는 방식'이다.

이를 좀 더 간단하게 말하면 서버에 요청을 보내는 방법이다. 

<br/>

- HTTP Method 종류

여러 가지 HTTP Method 중 가장 자주 사용되는 다섯 가지 method에 대해 설명하겠다. 

1. GET - data를 조회할 때 사용
2. POST - data를 생성할 때 사용
3. PUT - data를 수정할 때 사용(전체 수정)
4. PATCH - data를 수정할 때 사용(부분 수정)
5. DELETE - data를 삭제할 때 사용

<br/>

## Data 형식

Data를 주고받을 때 data를 표현하는 방식으로는 크게 JSON과 XML이 있다. JSON이 XML에 비해 가볍고 가독성이 좋아 요즘에는 JSON이 더 많이 쓰인다. 

<br/>

- JSON(JavaScript Object Notation)

데이터를 주고받을 때 가장 많이 사용되는 DATA 교환 형식이다. 

<br/>

## API(application programming interface)

API는 응용프로그램에서 데이터를 주고받기 위한 방법을 의미한다. 예를 들어 어떤 특정 사이트에서 데이터를 공유할 경우 어떠한 방식으로 정보를 요청해야 하고, 어떠한 데이터를 제공받을 수 있을지에 대한 규격들을 API라고 한다. 

<br/>

## RESTful API

Restful API는 REST 기반의 규칙들을 지켜서 설계한 API이다. 다른 말로 REST API라고도 한다. 

<br/>

- REST

rest는 웹의 장점을 최대한 활용할 수 있는 아키텍처이다. 

- REST API 디자인 가이드

1. URI는 정보의 자원을 표현한다. 그리고 자원을 표현할 때는 명사 복수형을 쓴다.
2. 자원에 대한 행위는 HTTP Method로 표현한다.

예시: 사용자 조회 API - GET /users, 사용자 정보 수정 API - PUT /users

<br/>

## Framework

library를 하나의 망치와 비유하자면 framework는 공구상자와 같다. 다시 말하면 여러 가지 도구들을 제공해 주는 큰 도구이다. 

