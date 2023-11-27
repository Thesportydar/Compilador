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
@errorEjecucion db "ERROR EJECUCION", 0
@recursividad db "RECURSIVIDAD DETECTADA", 0
@overflow db "OVERFLOW DETECTADO", 0
.code
CheckCallStack PROC
mov ecx, stackPointer
cmp ecx, 0
je NoRecursion
lea edx, [callStack + ecx * 4 - 4]
cmp DWORD PTR [edx], eax
je Recursion
jmp NoRecursion
Recursion:
invoke MessageBox, NULL, addr @recursividad, addr @errorEjecucion, MB_OK
invoke ExitProcess, 0
NoRecursion:
ret
CheckCallStack endp
PushToCallStack PROC
mov edx, stackPointer
mov DWORD PTR [callStack + edx * 4], eax
inc stackPointer
ret
PushToCallStack endp
PopFromCallStack PROC
dec stackPointer
mov edx, stackPointer
lea edx, [callStack + edx * 4]
mov eax, DWORD PTR [edx]
ret
PopFromCallStack endp
start:
invoke ExitProcess, 0
end start
