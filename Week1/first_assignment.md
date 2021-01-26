# 리눅스 환경에서 apm 수동설치 하기

<br/>

### - MacOs에서 리눅스 환경 만들기

MacOs에서 리눅스 환경을 만드는 방법은 여러가지 있다. 나는 그 중 Virtual machine을 이용해 리눅스 환경을 만들었다. 

Virtual machine을 이용해 ubuntu 설치하기. 

VM(Virtual machine)은 컴퓨터 환경을 소프트웨어로 구현한 것이다. 따라서 VM을 이용하면 내 컴퓨터의 원래 OS위에서 다른 OS를 실행할 수 있다. 

<br/>

나는 여러 VM중 Vmware Fusion을 이용했다. 그리고 그 위에 ubuntu 18.04을 설치했다. 

<br/>

### - apm 소스 설치(수동 설치, 컴파일 설치)

수동 설치는 패키지관리자가 해주는 일을 직접 사용자가 수행하는 것이다. 따라서 소스를 직접 다운받아 컴파일 하여 설치해야 한다. 그 과정은 다음과 같다.

1. 소스파일 다운받기.
2. ./configure이용해 설치하기 위한 환경 설정.
3. make이용 컴파일(소스파일을 이진파일로).
4. make install이용 설치하기.

위 과정을 좀더 자세히 알아보자. 2번 과정은 현재 OS의 종류나 컴파일러의 위치, 종류 등을 여러가지 설정을 통해 makefile을 만드는 과정이다. 3번 과정은 2번 과정에서 만든 makefile을 이용해 컴파일 하는 과정이다. 4번 과정은 컴파일된 프로그램, 환경 파일, 데이터 파일을 지정된 위치에 복사하는 과정이다. 

<br/>

- **Apache 2.4 소스 설치**

Apache는 HTTP 웹 서버로 Apache 재단에서 만든 소프트웨어이다. 

<br/>

Apache 2.4를 소스 설치하는 과정에 필요한 패키지들은 다음과 같다. 

```
apt-get install make // apt-get은 ubuntu의 패키지관리자
apt-get install gcc
apt-get install g++
apt-get install libexpat1-dev
apt-get intstall net-tools
apt-get install curl
```

<br/>

또 Apache을 설치하기 위해서는 APR, PCRE와 같은 의존성 패키지를 설치 해야한다. 

- **Apr, apr-util 소스 설치**

<br/>

1. 소스 다운 받기

```
# wget http://mirror.navercorp.com/apache//apr/apr-1.7.0.tar.gz
# wget http://mirror.navercorp.com/apache//apr/apr-util-1.6.1.tar.gz
# tar xvfz apr-1.7.0.tar.gz
# tar xvfz apr-util-1.6.1.tar.gz

```

*wget: web get의 약어. 웹상 파일 다운로드, tar xvfz: tar형식의 압축파일 풀기.

<br/>

2. apr, apr-util 설치

- apr설치 

```
$ cd usr/local/apr-1.7.0
$ ./configure --prefix=/usr/local/apr
$ make
$ make install
```

위 과정을 실행하기전 

```
cp -arp libtool libtoolT
```

명령어가 필요하다.

<br/>

- apr-util 설치

```
$ cd usr/local/apr-util-1.6.1
$ ./configure --with-apr=/usr/local/apr --prefix=/usr/local/apr-util 
$ make
$ make install
```

<br/>

- **pcre 설치**

<br/>

1. 소스 다운 받기

```
$ cd usr/local
$ wget ftp://ftp.pcre.org/pub/pcre/pcre-8.44.tar.gz
$ tar xvfz pcre-8.44.tar.gz
```

<br/>

2. 소스 설치

```
$ cd usr/local/pcre-8.44
$ ./configure --prefix=/usr/local/pcre
$ make
$ make install
```

<br/>

이제 의존성 패키지는 다 설치했으므로 apache을 소스 설치하자. 

- **Apache 2.4 소스 설치**

<br/>

1. 소스 다운 

```
$ cd /usr/local
$ wget http://apache.tt.co.kr//httpd/httpd-2.4.46.tar.gz
$ tar xvfz httpd-2.4.46.tar.gz
```

<br/>

2. 소스 컴파일 & 설치 

```
$ cd httpd-2.4.46
$ ./configure --prefix=/usr/local/apache2.4 \
--enable-module=so --enable-rewrite --enable-so \
--with-apr=/usr/local/apr \
--with-apr-util=/usr/local/apr-util \
--with-pcre=/usr/local/pcre \
--enable-mods-shared=all
$ make
$ make install
```

이제 Apache설치가 끝났다. 

<br/>

**Apache을 실행해 보자.** 

```
$ sudo /usr/local/apache2.4/bin/httpd -k start
$ ps -ef|grep httpd|grep -v grep
$ sudo netstat -anp|grep httpd
$ sudo curl http://127.0.0.1 // http정보 터미널로 가져오기.
```

이때 터미널에 It's Work!가 출력되면 정상적으로 실행 된 것이다. 

