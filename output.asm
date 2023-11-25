.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
callStack DWORD 100 DUP(?)
stackPointer DWORD 0
@2 dw ?
@3 dw ?
@5 dw 1
@6 dw 1
@8 dw 1
@9 dw ?
@11 dw 5
@aux0 dw ?
@aux1 dw ?
.code
factorial@global proc
mov eax, offsetfactorial@global
call CheckCallStack
call PushToCallStack
mov ax, @2
cmp ax, @5
jg END_IF0
mov ax, @6
mov @2, ax
jmp END_IF1
END_IF0:
mov ax, @2
sub ax, @8
mov @aux0, ax
mov ax, @aux0
mov @3, ax
mov ax, @2
imul @3
mov @aux1, ax
jo OverflowDetected
mov ax, @aux1
mov @2, ax
END_IF1:
call PopFromCallStack
ret
start:
mov ax, @11
mov @9, ax
mov ax, @9
mov @2, ax
call factorial@global
mov ax, @2
mov @9, ax
CheckCallStack PROC
mov ecx, stackPointer
cmp exc, 0
je NoRecursion
mov edx, DWORD PTR [callStack + (ecx - 1) * 4]
cmp edx, eax
je Recursion
jmp NoRecursion
Recursion:
invoke MessageBox, NULL, addr @recursividad, addr @recursividad, MB_OK
invoke ExitProcess, 0
NoRecursion:
ret
PushToCallStack PROC
mov edx, stackPointer
mov DWORD PTR [callStack + edx * 4], eax
inc stackPointer
ret
PopFromCallStack PROC
dec stackPointer
mov eax, DWORD PTR [callStack + stackPointer * 4]
ret
OverflowDetected:
invoke MessageBox, NULL, addr @overflow, addr @overflow, MB_OK
invoke ExitProcess, 0
invoke ExitProcess, 0
end start
