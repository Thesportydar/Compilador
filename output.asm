.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
.code
fun1@global@class1 proc
mov ax, @5
mov @2, ax
ret
start:
mov ax, @2
mov @8, ax
call fun1@global@class1
mov ax, @8
mov @2, ax
mov ax, @2
mov @19, ax
call fun1@global@class1
mov ax, @19
mov @2, ax
mov ax, @2
mov @30, ax
call fun1@global@class1
mov ax, @30
mov @2, ax
mov ax, @2
mov @38, ax
call fun1@global@class1
mov ax, @38
mov @2, ax
invoke ExitProcess, 0
end start
