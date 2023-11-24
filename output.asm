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
.code
start:
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
