# LEGv8-Disassembler

A disassembler for LEGv8 machine code as required by Computer Science 321 @ Iowa State University, Fall 2021.

### Supported Instructions

The supported instructions are the following:

* ADD
* ADDI
* AND
* ANDI
* B
* B.cond (This is a CB instruction in which the Rt field is not a register, but a code that indicates the condition
  extension)
	* 0: EQ
	* 1: NE
	* 2: HS
	* 3: LO
	* 4: MI
	* 5: PL
	* 6: VS
	* 7: VC
	* 8: HI
	* 9: LS
	* a: GE
	* b: LT
	* c: GT
	* d: LE
* BL
* BR (Rn field has the branch target)
* CBNZ
* CBZ
* EOR
* EORI
* LDUR
* LSL (shamt field encodes the shift amount, while Rm is unused)
* LSR (shamt field encodes the shift amount, while Rm is unused)
* ORR
* ORRI
* STUR
* SUB
* SUBI
* SUBIS
* SUBS
* MUL

Apart from these instructions, there are four additional instructions that serve the purposes of debugging with the
LEGv8 emulator provided in the course.

The following explanations are a snippet from the project requirements
by [Dr. Jeremy Sheaffer](https://www.cs.iastate.edu/sheaffer).

> * PRNT: This is an added instruction (part of our emulator, but not part of LEG or ARM) that prints a register name and
	> its contents in hex and decimal. This is an R instruction. The opcode is 11111111101. The register is given in the Rd
	> field.
> * PRNL: This is an added instruction that prints a blank line. This is an R instruction. The opcode is 11111111100.
> * DUMP: This is an added instruction that displays the contents of all registers and memory, as well as the disassembled
	> program. This is an R instruction. The opcode is 11111111110.
> * HALT: This is an added instruction that triggers a DUMP and terminates the emulator. This is an R instruction. The
	> opcode is 11111111111

### Disclaimer

The above code was authored by y-johnson (henceforth "I") for the purposes of a grade in Computer Science 321: "Computer
Architecture" by Dr. Jeremy Sheaffer. It is provided for the explicit purpose of showcasing work. Usage of this code for
any reason, especially that of a grade in the selfsame course, is not prohibited but not recommended due to academic
dishonesty. I am in no way liable for any cases of plagiarism and dishonesty concerning academics and also do not
guarantee proper functionality.