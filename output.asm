.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
@2 dw ?
@5 dw 1
@6 db " ENTRA EN LA FUNCION ", 0
@8 dw 2
@11 dw ?
@18 dw ?
@24 dw ?
@31 dw ?
@37 dw ?
@42 dw ?
@47 dw ?
@51 dw ?
@54 db "CHECK cls4.var1:global CUMPLIDO", 0
.code
fun1@global@class1 proc
mov ax, @5
mov @2, ax
invoke MessageBox, NULL, addr @6, addr @6, MB_OK
fun2@global@class1@fun1 proc
mov ax, @8
mov @2, ax
ret
fun2@global@class1@fun1 endp
ret
fun1@global@class1 endp
fun3@global proc
mov ax, @51
mov @47, ax
ret
fun3@global endp
start:
mov ax, @2
mov @11, ax
call fun1@global@class1
mov ax, @11
mov @2, ax
mov ax, @2
mov @24, ax
call fun1@global@class1
mov ax, @24
mov @2, ax
mov ax, @2
mov @37, ax
call fun1@global@class1
mov ax, @37
mov @2, ax
mov ax, @2
mov @47, ax
call fun1@global@class1
mov ax, @47
mov @2, ax
mov ax, @51
mov @47, ax
mov ax, @47
mov @51, ax
invoke MessageBox, NULL, addr @54, addr @54, MB_OK
invoke ExitProcess, 0
end start
