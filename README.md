# 2022-2-DatabaseSystem

## 개발 환경

* Eclipse

* MS SQL Server

* SQL Server Management Studio

<br>

## 프로젝트 소개

2022학년도 2학기 데이터베이스시스템 수업에서 수행한 팀 프로젝트이며 수행한 내용들은 아래와 같다.

1. 간단한 데이터베이스 설계

2. stored procedure, 사용자 정의 함수, trigger 를 생성

3. JDBC 를 이용해 자바 어플리케이션과 MS SQL Server 를 연동

4. JavaFX 를 이용해 GUI 를 구현한다

5. 어플리케이션에서 stored procedure를 호출해 데이터를 수정, 삽입, 삭제할 수 있다

6. 어플리케이션에서 데이터베이스에 구현된 테이블과 속성명, 데이터, procedure, 사용자 정의 함수, trigger 등을 확인할 수 있다

7. Jasper Report 를 이용해 보고서를 만들고, GUI 에서 print 버튼을 눌렀을 때 해당 보고서를 출력한다


<br>


## 세부 사항

### 1. 데이터베이스 설계

![table](https://user-images.githubusercontent.com/112313795/235467059-a9e1cd51-3936-42ee-9a7d-0afa08518f21.png)


간단하게 교수, 논문, 학생, 과목 테이블을 가진 데이터베이스를 설계하였다


<br>



### 2. stored procedure 생성

![procedure](https://user-images.githubusercontent.com/112313795/235467128-bdeb9ecc-e907-4711-94c5-0f15c6b480ef.png)


어플리케이션에서 데이터에 대한 삽입, 수정, 삭제가 가능하도록 모든 테이블마다 삽입, 수정, 삭제에 해당하는 프로시저를 생성하였다



<br>



### 3. 사용자 함수 정의

![function](https://user-images.githubusercontent.com/112313795/235467168-458ce971-7fe0-41ae-ad5b-9d5ef4c86b31.png)


DB에서 사용할 수 있도록 사용자 정의 함수를 생성하였고, 스칼라 반환 함수, 테이블 반환 함수를 나누어서 생성하였다



<br>



### 4. trigger 생성

![trigger](https://user-images.githubusercontent.com/112313795/235467196-8ccbe5c7-4692-4384-ab05-140a5cd6fc08.png)


특정 테이블에 삽입, 수정, 삭제가 일어나면 수행할 trigger 를 생성하였다



<br>



### 5. GUI 기본 구성

![gui](https://user-images.githubusercontent.com/112313795/235467230-465615c0-2542-4412-8652-d74ffc4c7d62.png)


테이블을 클릭하면 DB에 존재하는 테이블들을 출력하고, 테이블을 클릭하면 해당 테이블이 가진 데이터를 출력한다



<br>


### 6. 삽입

![save](https://user-images.githubusercontent.com/112313795/235467271-fe66dd55-2b6f-4950-9c63-d0f91698a19e.png)



데이터를 입력하고 save 버튼을 누르면 테이블에 데이터가 삽입되고, 하단에 record is added... 라는 메세지를 출력한다



<br>


### 7. 수정

![update](https://user-images.githubusercontent.com/112313795/235467299-b4fb6755-b735-4042-9e01-ea40622d4390.png)


데이터를 입력하고 update 버튼을 누르면 데이터가 수정되고, 하단에 record is updated... 라는 메세지를 출력한다


<br>


### 8. 삭제

![delete](https://user-images.githubusercontent.com/112313795/235467327-79e323ba-3324-414d-a654-336d8e6720e3.png)


데이터를 선택하고 delete 버튼을 누르면 데이터가 삭제된다




<br>


### 9. 보고서 출력

![print](https://user-images.githubusercontent.com/112313795/235467355-1d713843-0284-4113-be6a-df2d44a3fde2.png)


테이블을 클릭하고 상단의 print 버튼을 누르면 위와 같이 미리 작성된 보고서 템플릿에 데이터가 삽입되어 출력된다



<br>


### 10. DB 정보 출력 ( 1 )

![about](https://user-images.githubusercontent.com/112313795/235467406-6822fe2d-7b70-4109-b096-763dfc2687a1.png)


DB 이름, 테이블 수, 프로시저 수 등과 같은 정보들을 확인할 수 있는 기능이다


<br>


### 11. DB 정보 출력 ( 2 )

![all](https://user-images.githubusercontent.com/112313795/235467426-d0e4fbc4-9b9b-45ba-99e4-276267da0e3c.png)

![viewProcedure](https://user-images.githubusercontent.com/112313795/235467453-d88d4f75-b85f-4915-80f7-12f656869d62.png)


테이블과 속성, DB에 정의된 스칼라 반환 함수, 테이블 반환 함수, 프로시저와 trigger 이름들이 출력된다
