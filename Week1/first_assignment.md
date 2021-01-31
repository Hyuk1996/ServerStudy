# 리눅스 환경에서 apm 수동설치 하기

<br/>

## - MacOs에서 리눅스 환경 만들기

MacOs에서 리눅스 환경을 만드는 방법은 여러 가지 있다. 나는 그중 Virtual machine을 이용해 리눅스 환경을 만드는 방법을 이용했다.

VM(Virtual machine)은 컴퓨터 환경을 소프트웨어로 구현한 것이다. 따라서 VM을 이용하면 내 컴퓨터의 원래 OS 위에서 다른 OS를 실행할 수 있다. 

<br/>

저는 여러 VM 중 Vmware Fusion을 이용했다. 그리고 그 위에 ubuntu 18.04를 설치해 리눅스 환경을 만들었다.  

<br/>

<br/>

## - apm(apache, php, mysql) 소스 설치(수동 설치, 컴파일 설치)

- **소스 설치(수동 설치)?**

수동 설치는 사용자가 직접 소스를 받아와 컴파일하는 방식으로 프로그램을 설치하는 유닉스의 전통적인 방식이다. 위와 같이 프로그램을 설치하려면 과정도 번거롭고 또 버전이 업그레이드될 때마다 다시 컴파일 해줘야 한다. 요약하자면 설치 과정이 많이 번거롭다. 물론 위 작업을 간단하게 해주는 패키지 매니저를 이용해 설치를 해도 된다. 하지만 가끔씩 보안상의 이유로 패키지 매니저를 사용하지 못하는 경우가 있을 수 있다. 따라서 수동 설치 방법을 알아둘 필요가 있다. 

<br/>

- **소스 설치 과정**

1. 소스 파일 다운
2. ./configure 
3. make
4. make install

위 과정을 좀더 자세히 설명하겠다. 

1번 과정은 말 그대로 소스 코드를 받아와서 내 컴퓨터에 저장하는 과정이다. 

2번 과정은 1번 과정에서 받아온 소스 파일들을 어떻게 compile할 것인지 makefile을 만들어 주는 과정이다.

3번 과정은 2번 과정에서 만든 makefile을 가지고 compile을 하는 단계이다. 

4번 단계는 3번 단계에서 만들어진 파일들을 알맞은 위치에 설치하는 과정이다. 

<br/>

<br/>

- **Apache 2.4 소스 설치**

Apache는 HTTP 웹 서버로 Apache 재단에서 만든 소프트웨어이다. 

<br/>

Apache 2.4를 소스 설치하는 과정에 필요한 기본적인 패키지들은 다음과 같다. 

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

<br/>

<br/>

- **Apr, apr-util 소스 설치**

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

<br/>

<br/>

- **Apache 2.4 소스 설치**

1. 소스 다운

```
$ cd /usr/local
$ wget http://apache.tt.co.kr//httpd/httpd-2.4.46.tar.gz
$ tar xvfz httpd-2.4.46.tar.gz
```

<br/>

2. 소스 설치

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

- **Apache을 실행해 보자.** 

```
$ sudo /usr/local/apache2.4/bin/httpd -k start // stop을 쓰면 종료
$ ps -ef|grep httpd|grep -v grep
$ sudo netstat -anp|grep httpd
$ sudo curl http://127.0.0.1 // http정보 터미널로 가져오기.
```

이때 터미널에 It's Work!가 출력되면 정상적으로 실행 된 것이다. 

<br/><br/>

- **mysql 8 소스 설치**

1. mysql을 설치하기 전

mysql은 RDBMS이다. 즉 관계형 데이터베이스를 관리해 주는 시스템이다. 

<br/>

mysql을 설치 하기전에 설치해야할 의존성 패키지들이 있다. 

1. CMake
2. GNU make 3.75이상
3. GCC 5.3 이상
4. C++ 또는 C99 컴파일러
5. SSL library
6. Boost C++ library
7. ncurseslibrary
8. Perl

해당 패키지들을 apt-get으로 설치한 사실을 확인하고 싶으면 다음과 같은 명령어를 이용하면 된다. 

```
$ dpkg -l | grep [이름]
```

<br/>

저는 확인해본 결과 1, 5, 6, 7 패키가 없어서 다음과 같이 apt-get을 이용해 설치했다. 

```
$ apt-get update
$ apt-get install cmake
$ apt-get install libssl-dev
$ apt-get install libboost-all-dev
$ apt-get install libncurses5-dev libncursesw5-dev
```

<br/>

이제 본격적으로 mysql을 설치해보자. 

<br/>

2. 소스 다운

[설치 링크]( https://dev.mysql.com/downloads/mysql/) 여기 링크로 가면 소스 파일의 링크를 알 수 있다. 그 후 다음 명령어들을 이용해 소스를 다운 받았다.

```
$ cd /usr/local
$ wget https://dev.mysql.com/get/Downloads/MySQL-8.0/mysql-8.0.21.tar.gz
$ tar xvfz mysql-8.0.21.tar.gz
```

<br/>

2. 소스 설치

mysql은 apache와 다르게 cmake을 이용해 makefile을 만든다. 또 mysql은 빌드시에 하단에 디렉토리를 만들어 작업하도록 권고하기 때문에 디렉토리를 만들어 그곳에서 빌드를 진행했다. 

```
$ cd /usr/local/mysql-8.0.21
$ mkdir hyukmysql01
$ cd usr/local/mysql-8.0.21/hyukmysql01

$ cmake \
.. \
-DCMAKE_INSTALL_PREFIX=/usr/local/mysql \
-DMYSQL_DATADIR=/usr/local/mysql/data \
-DMYSQL_UNIX_ADDR=/usr/local/mysql/mysql.sock \
-DMYSQL_TCP_PORT=3306 \
-DDEFAULT_CHARSET=utf8 \
-DDEFAULT_COLLATION=utf8_general_ci \
-DSYSCONFDIR=/etc \
-DWITH_EXTRA_CHARSETS=all \
-DWITH_INNOBASE_STORAGE_ENGINE=1 \
-DWITH_ARCHIVE_STORAGE_ENGINE=1 \
-DWITH_BLACKHOLE_STORAGE_ENGINE=1 \
-DDOWNLOAD_BOOST=1 \
-DWITH_BOOST=/usr/local/mysql/boost 

$ make
$ make test 
$ make install
```

위와 같이 하면 mysql이 설치된다. 

<br/>

<br/>

- **mysql 초기화 하기**

[출처](https://dev.mysql.com/doc/refman/8.0/en/data-directory-initialization.html#data-directory-initialization-server-actions)

mysql을 소스 설치할 경우 data directory초기화가 되지 않으므르 직접 초기화 해줘야 한다. 

<br/>

1. Mysql 그룹과 유저 생성

시스템에 mysql을 수행할 user와 group이 없으면 생성해 줘야한다. 과정은 다음과 같다.

```
$ groupadd mysql 
//mysql이라는 그룹을 지어서 관리

$ useradd -r -g mysql -s /bin/false mysql
// 사용자 추가 useradd
// -r : 시스템 계정
// -g : 그룹 지정
// -s : 로그인 쉘 지정
// 마지막 mysql은 사용자의 이름
```

<br/>

2. Mysql-files directory 만들기

Secure_file_priv가 mysql-files에 접근해야 하므로 만들어 줘야한다. 

```
$ cd /usr/local/mysql
$ mkdir mysql-files

$ chown -R mysql:mysql /usr/local/mysql
$ chown mysql:mysql mysql-files
$ chmod 750 mysql-files
```

<br/>

3. Data Directory 초기화

Data directory는 생성된 database들이 저장되는 위치이다. 

서버를 이용해 data directory를 초기화 해줘야 한다. 

```
$ bin/mysqld --initialize --user=mysql \
--basedir=/usr/local/mysql \
--datadir=/usr/local/mysql/data
```

위 과정의 마지막 단계에서 임시 비밀번호가 출력된다. 이는 로그인시 필요하니 기억해둬야 한다.

<br/>

4. root계정 암호 초기화

- 먼저 mysql 서버를 시작하자. 

```
$ bin/mysqld_safe --user=mysql &
```

<br/>

- mysql이 잘 실행되고 있는지 확인하기.

```
$ ps -ef | grep mysqld
```

<br/>

- mysql에 접속하기.

```
$ bin/mysql -u root -p
```

<br/>

- mysql에서 비밀번호 변경하기.

```
ALTER USER 'root'@'localhost' IDENTIFIED BY '사용할 비밀번호';
```

mysql에서 나오고 싶으면 quit을 입력하면 나올 수 있다.

<br/>

- mysql 서버 종료하기.

```
$ bin/mysqladmin -u root -p shutdown
```

<br/>

<br/>

- **mysql 환경설정**

mysql 옵션 파일은 mysql이 실행될 때마다 읽힌다. 따라서 옵션파일을 설정하면 편리하게 사용할 수 있다. 

```
vi /etc/my.cnf // 옵션 파일 위치

//vi에서 다음과 같이 입력
[mysqld]
bind-address=0.0.0.0
port=3306
basedir=/usr/local/mysql
datadir=/usr/local/mysql/data
```

<br/>

<br/>

- **mysql 서비스 등록**

컴퓨터를 부팅할 때마다 mysql server을 직접 시작해야 하는 번거로움을 없애기 위해 서비스 등록이 필요하다. 서비스 등록을 해두면 컴퓨터가 부팅 시 자동으로 mysql server가 시작되고 간단한 명령어로 종료 재시작 할 수 있다. 

<br/>

과정은 다음과 같다. 

1. mysqld 파일 복사

```
$ sudo cp /usr/local/mysql/support-files/mysql.server /etc/init.d/mysqld
```

<br/>

2. mysqld 파일에 basedir, datadir 설정

```
$ sudo vi /etc/init.d/mysqld
```

basedir=/usr/local/mysql

datadir=/usr/local/mysql/data

<br/>

3. mysqld 서비스 등록

```
update-rc.d mysqld defaults
```

<br/>

- 간단한 명령어

```
$ service mysql start
$ service mysql stop
$ service mysql restart //서버를 중지했다가 다시 시작하는거
$ service mysql status //서버 상태 확인. 나가려면 q 누르기
```

<br/>

<br/>

- **php 소스 설치**

php 설치에 필요한 의존성 패키지 설치

```
$ apt-get install libxml2-dev
$ apt-get install libjpeg-dev
$ apt-get install libpng-dev
$ apt-get install libsqlite3-dev
```

<br/>

1. 소스 다운

```
$ cd /usr/local
$ wget https://www.php.net/distributions/php-7.4.8.tar.gz
$ tar xvfz php-7.4.8.tar.gz
```

<br/>

2. 소스 설치

```
$ cd php-7.4.8
$ ./configure \
--with-apxs2=/usr/local/apache2.4/bin/apxs \
--enable-mysqlnd \
--with-mysql-sock=mysqlnd \
--with-mysqli=mysqlnd \
--with-pdo-mysql=mysqlnd \
--with-imap-ssl \
--with-iconv \
--enable-gd \
--with-jpeg \
--with-libxml \
--with-openssl

$ make
$ make test
$ make install
```

위와 같이 설치해주면 /usr/local/apache2.4/modules/ 에 libphp7.so가 설치 되었음을 확인할 수 있다. 

<br/>

<br/>

- **apache와 php 연동하기**

<br/>

1. /usr/local/apache2.4/conf/httpd.conf 설정 파일에 PHP 모듈이 있는지 확인한다. 

![1](https://user-images.githubusercontent.com/29492667/105990017-39e4b800-60e5-11eb-9cc4-3dcc5ff4252a.jpg)

확인 되었으면 vi를 이용해 해당 파일에 다음과 같이 한 줄을 추가해 준다. 

![2](https://user-images.githubusercontent.com/29492667/105990101-600a5800-60e5-11eb-9785-959a1c1fb512.jpg)

<br/>

2. php.ini 파일 세팅

php.ini 는 php 의 설정 파일이다. php configure 시에 옵션으로 위치를 설정하지 앟으면 /usr/local/lib/php.ini 을 사용한다. 

```
$ cd /usr/local/php-7.4.8
$ cp php.ini-production /usr/local/lib/php.ini
```

<br/>

<br/>

- **테스트 하기**

apache의 html, php 파일은 기본적으로 /htdocs에 위치한다. 

<br/>

1. 테스트할 php 파일 만들기 

```
$ cd /usr/local/apache2.4/htdocs

$ vi phpinfo.php
<? php
phpinfo();
?>
```

<br/>

2. apache을 실행시키고 php파일 불러오기.

```
$ sudo /usr/local/apache2.4/bin/httpd -k start
//아파치 실행 명령

$ ps -ef|grep httpd|grep -v grep
//아파치 실행중인지 확인

$ sudo netstat -anp|grep httpd

$ sudo curl http://127.0.0.1/
//curl 명령으로 로컬호스트로 아파치 켜졌는지 확인(It's Work!)
```

브라우저 이용 http://127.0.0.1/phpinfo.php로 접속해서 다음 화면이 나오면 연동된것이다. 

![3](https://user-images.githubusercontent.com/29492667/105990125-67c9fc80-60e5-11eb-8505-a957ca929be9.jpg)

