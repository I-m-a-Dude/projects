.386
.model flat, stdcall
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;includem biblioteci, si declaram ce functii vrem sa importam
includelib msvcrt.lib
extern exit: proc
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;declaram simbolul start ca public - de acolo incepe executia
public start
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;sectiunile programului, date, respectiv cod
.data
;aici declaram date
;bloc dw 4 DUP(22);
tabel db 3 DUP(2 DUP(33h));
Octeti DB 3,13,13h,00010011b;
Vector dw 3,35h, 1a3bh,0CDEh;
;C db 011h,01000100B;
litere db 'm','a','s'',a';
var dw 1314h or 00FFH;
;caractere db 41h,"text";
D dd 11aa33efh,12345678h;
  dw 4 DUP(133h);
.code
start:
	;aici se scrie codul
	
	;terminarea programului
	push 0
	call exit
end start
