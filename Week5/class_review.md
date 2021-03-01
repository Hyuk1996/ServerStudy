# 5주차 수업

<br>

## 목차

- validation
- 로그인 유지 방식

<hr/>

<br/>

## validation

<br>

- validation 이란?

클라이언트가 request를 보낼 때 해당 request가 형식은 적절한지, 의미 있는 data 인지를 확인하는 작업을 validation 이라고 한다. 

validation 과정은 형식적 validation과 의미적 validation으로 나눌 수 있다. 

<br>

형식적 validation은 해당 request의 길이, 값의 타입 등 형식적인 요소가 알맞게 요청되었는지를 확인한다. Spring boot에서는 주로 controller에서 처리해 준다. 

의미적 validation은 request 온 정보가 database에 있는지, 아니면 없는지 등 의미가 있는 data 인지를 확인한다. Spring boot에서는 주로 provider/service 단에서 처리해 준다. 

<br/>

validation 과정은 백엔드를 개발할때 필수적인 단계이다. validation을 제대로 처리해 주지 않으면 서버가 터질 수도 있고, DB가 망가질 수도 있다.  

<hr/>

<br/>

## 로그인 유지 방식 

<br/>

- 로그인 유지 방식이 필요한 이유

http는 connectionless, stateless 특징을 가지고 있기 때문에 로그인 유지 방식을 쓰지 않으면 원하는 정보를 가져오려 할때마다 다시 로그인을 해야하는 불편함이 있다. 

<br>

conntectionless: client가 server에게 request를 보내면 그에 대한 response를 보내고 바로 연결을 끊는다. 

stateless: server는 접속한 client의 정보를 남겨놓지 않는다. 

<br/>

- 로그인 유지 방식

로그인 유지 방식에는 크게 세 가지가 있다. 

1. 쿠키, 세션
2. oauth
3. jwt

각각을 놀이공원의 입장권에 비유하자면 쿠키 세션은 '자유이용권'과 같고, oauth는 '빅 5 이용권'과 같고, jwt는 '즉석발권' 이다. 

<br/>

- 쿠키, 세션?

처음에 client가 server에 id와 password를 주고 로그인을 요청 -> server는 해당 요청을 받고 유효한 id, password인지 확인 -> (유효하면) server에서는 session을 만든 뒤 session id를 cookie를 만들어 그곳에 넣고 client에게 response-> client은 받은 cookie를 local에 저장해 두고 server에 접속 할때마다 cookie을 같이 보내면 server는 해당 session에 상태들을 저장해두고 맞는 response를 보냄

<br/>

<br/>

- oauth

간단하게 말하면 로그인을 내 서버에서 직접해주는 것이 아니라, 대형 플렛폼 예를 들면 naver.kakao와 같은 곳에서 하는 것이다. 

그러면 사용자는 내 서버에 따로 가입 없이 서버의 API들을 사용할 수 있다. 

서버 입장에서는 사용자의 정보들을 naver,kakao에게 받아 사용하면 된다.

<br/>

<br/>

- jwt(json web token) (우리가 사용할 방식)

Jwt는 다음과 사용됩니다.

client가 server에게 id/password로 로그인 request -> server는 해당 request들이 유효하면 해당 정보들을 이용해 jwt을 만들어 client에게 response

<br/>

그 후로 client는 request를 보낼 때 header에 jwt를 넣어서 보내주면 server는 jwt를 해석해 권한이 있는 작업들을 수행 후 그 결과를 request 해준다. 

<br/>

- Jwt 구조 

JWT 는 header + payload + signature로 이루어져 있다. 

header: header에는 token의 type과 해싱 type이 들어간다. 

payload: token에 들어갈 정보들이 들어간다. 예를들면 아이디, 로그인 시간...

Signature:  header와 paload의 정보들을 합친 후 비밀키를 이용해 인코딩한 정보들이 들어간다. 





