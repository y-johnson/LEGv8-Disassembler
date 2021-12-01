# Notes
## Sequence
1. Retrieve file specified in program argument #1.
2. Read every line of the given file. 
   > "It's a binary file, in big endian byte order. Exactly 4 bytes per instruction." - Sheaffer
3. Convert each line into an equivalent LEGv8 instruction.
   1. Branches must branch to a label (the name itself does not matter)
4. Output into the terminal what would be the result of the previous' step after being applied to the whole program.
5. 
## Implementation
* The program must load the file's contents into memory. 
  * A BufferedReader pointing to the given file should suffice.
  * Storing each individual line into a Collection or List would allow sequential iteration.
* The program must parse the contents to translate them into LEGv8 Assembly.
  * A dictionary that reads their format and then their opcodes would be ideal.
  * Store the translated value into another Collection/List to preserve the order.
  * For branches, labels can be retrofitted to the assembly line at (current line's index + branch offset).
* The program must print out its contents into the terminal.

## Need
* ~~A way to read every 4 bytes of a file in binary.~~
* ~~A dictionary/lookup table/index to reference when converting from binary to assembly.~~
* A class that encapsulates instructions details and the string representation of them.
* A labeling algorithm.