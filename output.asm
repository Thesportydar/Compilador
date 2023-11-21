.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
@1 dd ?
@2 dd ?
@5 dd 1
@6 dd 1
@7 dd 1
@8 dd 1
@9 dd 3
@10 dd 2
@11 dd 3
@12 dd 2
@13 dd 2
@14 dd 2
.code
mov ax, @1
cmp ax, @2
jne END_IF1
mov ax, @5
mov @1, ax
mov ax, @6
mov @1, ax
mov ax, @7
mov @1, ax
mov ax, @1
cmp ax, @8
jne END_IF0
mov ax, @9
mov @1, ax
jmp END_IF0
END_IF0:
mov ax, @10
mov @1, ax
mov ax, @11
mov @2, ax
jmp END_IF2
END_IF1:
mov ax, @12
mov @1, ax
mov ax, @13
mov @1, ax
mov ax, @14
mov @1, ax
END_IF2:
