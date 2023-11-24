.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
@1 dw ?
@2 dw ?
@4 dw 1
@6 dw 2
@7 dw 1
@8 dw 2
@9 db " ENTRA EN EL WHILE ", 0
@10 db " hola ", 0
@aux0 dw ?
@aux1 dw ?
.code
start:
mov ax, @4
mov @1, ax
mov ax, @6
mov @2, ax
START_WHILE0:
mov ax, @1
add ax, @7
mov @aux0, ax
mov ax, @aux0
mov @1, ax
mov ax, @8
cmp ax, 0
mov ax, @aux0
idiv @8
mov @aux1, ax
mov ax, @aux1
cmp ax, @2
je END_WHILE0
invoke MessageBox, NULL, addr @9, addr @9, MB_OK
jmp START_WHILE0
END_WHILE0:
mov ax, @1
cmp ax, @2
je label0
jmp END_IF0
label0:
invoke MessageBox, NULL, addr @10, addr @10, MB_OK
jmp END_IF0
END_IF0:
invoke ExitProcess, 0
end start
