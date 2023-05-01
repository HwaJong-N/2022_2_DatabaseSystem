use Project
go

-- insert
create proc sp_insertPaper @c1 varchar(5), @c2 varchar(100), @c3 varchar(3), @c4 varchar(100)
as
begin
insert into dbo.Paper values(@c1, @c2, @c3, @c4)
end
go

create proc sp_insertProfessor @c1 varchar(5), @c2 varchar(20), @c3 varchar(40), @c4 varchar(3)
as
begin
insert into dbo.Professor values(@c1, @c2, @c3, @c4)
end
go

create proc sp_insertResearch @c1 varchar(5), @c2 varchar(5), @c3 date, @c4 date
as
begin
insert into dbo.Research values(@c1, @c2, @c3, @c4)
end
go

create proc sp_insertStudent @c1 varchar(5), @c2 varchar(20), @c3 int, @c4 varchar(40), @c5 varchar(5)
as
begin
insert into dbo.Student values(@c1, @c2, @c3, @c4, @c5)
end
go

create proc sp_insertSubject @c1 varchar(5), @c2 varchar(40), @c3 int, @c4 varchar(5), @c5 int, @c6 varchar(3)
as
begin
insert into dbo.Subject values(@c1, @c2, @c3, @c4, @c5, @c6)
end
go

create proc sp_insertTake @c1 varchar(5), @c2 varchar(5), @c3 bit
as
begin
insert into dbo.Take values(@c1, @c2, @c3)
end
go



--delete
create proc sp_deletePaper @c1 varchar(5)
as
begin
delete from dbo.Paper
	where PNo = @c1
end
go

create proc sp_deleteProfessor @c1 varchar(5)
as
begin
delete from dbo.Professor
	where PID = @c1
end
go

create proc sp_deleteResearch @c1 varchar(5), @c2 varchar(5)
as
begin
delete from dbo.Research
	where PNo = @c1 and PID = @c2
end
go

create proc sp_deleteStudent @c1 varchar(5)
as
begin
delete from dbo.Student
	where ST_ID = @c1
end
go

create proc sp_deleteSubject @c1 varchar(5), @c2 varchar(5)
as
begin
delete from dbo.Subject
	where SU_ID = @c1 and PID = @c2
end
go

create proc sp_deleteTake @c1 varchar(5), @c2 varchar(5)
as
begin
delete from dbo.Take
	where ST_ID = @c1 and SU_ID = @c2
end
go


--update
create proc sp_updatePaper @c1 varchar(5), @c2 varchar(25), @c3 varchar(15), @c4 varchar(8)
as
begin
update dbo.Paper
	set PTitle = @c2, PPage = @c3, PAcademy = @c4
	where PNo = @c1
end
go

create proc sp_updateProfessor @c1 varchar(5), @c2 varchar(25), @c3 varchar(15), @c4 varchar(8)
as
begin
update dbo.Professor
	set PName = @c2, PMajor = @c3, PLab = @c4
	where PID = @c1
end
go

alter proc sp_updateResearch @c1 varchar(5), @c2 varchar(5), @c3 date, @c4 date
as
begin
update dbo.Research
	set start_date = @c3, end_date = @c4
	where PNo = @c1 and PID = @c2
end
go

create proc sp_updateStudent @c1 varchar(5), @c2 varchar(20), @c3 int, @c4 varchar(40), @c5 varchar(5)
as
begin
update dbo.Student
	set ST_Name = @c2, ST_Grade = @c3, ST_Major = @c4, PID = @c5
	where ST_ID = @c1
end
go

alter proc sp_updateSubject @c1 varchar(5), @c2 varchar(40), @c3 int, @c4 varchar(5), @c5 int, @c6 varchar(3)
as
begin
update dbo.Subject
	set SU_Name = @c2, SU_Credit = @c3, PID = @c4, lec_time = @c5, lec_class = @c6
	where SU_ID = @c1
end
go

create proc sp_updateTake @c1 varchar(5), @c2 varchar(5), @c3 bit
as
begin
update dbo.Take
	set retake = @c3
	where ST_ID = @c1 and SU_ID = @c2
end
go




-- function
create function getCols(@tname varchar(30))
returns table as
return SELECT COLUMN_NAME, DATA_TYPE
from INFORMATION_SCHEMA.COLUMNS
where TABLE_NAME=@tname
go


create function getTables()
returns table as
return select name, create_date from sys.tables
go


create function getProcedures()
returns table as
return select name, create_date from sys.procedures
go



create function getTableValueFunc()
returns table as
return (select name, create_date from sys.objects as o where o.type = 'IF')
go


create function getScalarFunc()
returns table as
return (select name, create_date from sys.objects as o where o.type = 'FN')
go


create function getTriggers()
returns table as
return (select name, create_date from sys.triggers)
go


-- scalar function

create function getPaperCount()
returns int as
begin
declare @cnt int;
select @cnt = count(PNo) from Paper
return @cnt
end
go


create function getProfessorCount()
returns int as
begin
declare @cnt int;
select @cnt = count(PID) from Professor
return @cnt
end
go


create function getStudentCount()
returns int as
begin
declare @cnt int;
select @cnt = count(ST_ID) from Student
return @cnt
end
go


-- trigger

create trigger paperTrigger 
on Paper
after insert, delete, update	
as
begin
print 'there is a change in Paper table'
end
go

create trigger professorTrigger 
on Professor
after insert, delete, update	
as
begin
print 'there is a change in Professor table'
end
go

create trigger studentTrigger 
on Student
after insert, delete, update	
as
begin
print 'there is a change in Student table'
end
go

use Project
go

alter proc testProc
as
begin
select name, create_date from sys.tables
end
go

exec testProc