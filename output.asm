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
fun1@global@class2 proc
mov ax, @1
mov @2, ax
ret
fun2@global@class2 proc
mov ax, @14
mov @2, ax
ret
fun1@global proc
fld @15
fstp @15
ret
start:
mov ax, @1
mov @1, ax
mov ax, @2
mov @2, ax
mov ax, @1
mov @2, ax
call fun1@global@class2
mov ax, @23
mov @2, ax
invoke ExitProcess, 0
end start
