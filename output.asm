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
@1 dw ?
@3 dw 3
@4 dw 0
@5 db "var1 es mayor que cero", 0
@6 db "var1 es menor o igual que cero", 0
@7 dw 0
@8 db "var1", 0
@9 dw 1
@aux0 dw ?
@recursividad db "RECURSIVIDAD", 0
@overflow db "OVERFLOW", 0
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
invoke MessageBox, NULL, addr @recursividad, addr @recursividad, MB_OK
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
mov ax, @3
mov @1, ax
mov ax, @1
cmp ax, @4
jl END_IF0
je END_IF0
invoke MessageBox, NULL, addr @5, addr @5, MB_OK
jmp END_IF1
END_IF0:
invoke MessageBox, NULL, addr @6, addr @6, MB_OK
END_IF1:
START_WHILE0:
mov ax, @1
cmp ax, @7
jl END_WHILE0
je END_WHILE0
invoke MessageBox, NULL, addr @8, addr @8, MB_OK
mov ax, @1
sub ax, @9
mov @aux0, ax
mov ax, @aux0
mov @1, ax
jmp START_WHILE0
END_WHILE0:
invoke ExitProcess, 0
end start
