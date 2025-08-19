## CPU Simulator
* 8-bit CPU simulator in Java
* Includes an assembler, a sample device driver, and unit tests
* Please refer to the accompanying document
* Simple teaching/learning tool for low-level systems programming
* Has atomic test/set/wait, trap/singleStep, M1 clock simulation
* Next steps: comments, cleanup, 16-bit addresses/registers
* Yes, it is designed to NOT be fast or efficient! Why?
* Because it models computer hardware, even Bits, as objects.
* I'm 75 and retired, so this is just a toy project 
* My Email: jim.clack@ablestrategies.com

## Topics for Discussion
* Bits are numbered right-to-left from zero - i.e. 7 6 5 4 3 2 1 0
* Bit weights are powers-of-two - i.e. 128, 64, 32, 16, 8, 4, 2, 1 (units)
* Two's compliment notation (negative values, numeric/logical invert)
* IRQ/ISR handling, concurrency, i/o ports, device drivers, M1 clock interrupt

## How to use it, the basics
~~~
CPU cpu = new CPU();
Assembler asm = new Assembler(cpu);
asm.assemble("Buf: EQU 0x51; LOADIMM $3, 7; STORMEM $3, Buf: TRAP");
cpu.run(false);
~~~

## How to do debugging, concurrency, M1 clock, ...
~~~
String myAsmCode = "...put your ASM source code here...";
CPU cpu = new CPU();
Assembler asm = new Assembler(cpu);
int errorCount = asm.assemble(myAsmCode);
cpu.setTracingDelayMs(200); // ms -1 = disable
cpu.activateM1Clock(millis, IRQ); // IRQ 0 = disable
CPU.RunMode runMode = cpu.run(false);
~~~

## Running a device simulator and driver
~~~
class MyDevice implements ICallableDevice {
  static int OutputPort = 10;
  static int InputPort = 11;
  static int InputIRQ = 5;
  MyDevice() {
    // 1. Install a device driver
    CPU cpu = new CPU();
    new Assembler(cpu).assemble(driverSourceCode);
    // 2. Wire this device simulator to the CPU
    cpu.ioPorts[OutputPort].setDeviceCallback(this);
    cpu.ioPorts[InputPort].setInterruptNumber(InputIRQ);
    // 3. Create a kbd input simulator for the CPU
    Thread simulator = new Thread() {
      public void run() { 
        System.out.print("Typoe 0..99 and hit Enter>");
        Scanner keyboard = new Scanner(System.in);
        while(true) {
          String command = keyboard.nextLine();
          if (!command.isEmpty()) {
            int value = Integer.parseInt(command);
            cpu.getIoPort(InputPort).inputToCpu(value);
          }
        }
      }
    };
    simulator.setDaemon(true);
    simulator.start();
    // 4. Run it...
    cpu.run(false);
  }
  // 5. Handle console output from the CPU simulator
  public int acceptOutputFromCPU(int value) {
    System.out.println("\n" + value);
    return 0; // no interrupt needed
  }
  // 6. Here's the device driver source code
  static String driverSourceCode = """
           JMPIMM Begin:       # Skip over data area
  OutPort: EQU 10
  Irq:     EQU 5
  InPort:  EQU 11
  Buf:     0                   # Inp -> Buf: -> Out
  Begin:   LOADIMM $IV, Isr:   # Enable interrupts
  Loop:    ZEROREG $1
           ADDMEM $1, Buf:     # Add to set Flags
           JZEIMM Loop:        # Wait for Buf != 0
           INCREG $1           # Increment InputVal 
           OUTREG $1, OutPort: # Echo it
           ZEROREG $2
           STORMEM $2, Buf:    # Clear the Buffer
           JMPIMM Loop:        # Else keep looping
  # Here's the ISR...
  Isr:     ENTER 0             # When input is rcv'd...
           CMPIMM $IN, Irq:    # Make sure it's for us
           JNZEIMM Leave:      # If not Our IRQ
           INPREG $2, InPort:  # Read it
           STORMEM $2, Buf:    # Put it into Buf
  Leave:   ILEAVE
  Exit:
  """;
}
~~~

## Quick Ref...
* Look in CPU.java for up-to-date General reference
* Look in Opcode.java for up-to-date Opcodes reference
* Look in Assembler.java for up-to-date Source Code notes
* I've duplicated them all below...

~~~
CPU Notes
 * Memory Layout using C/C++ calling convention
 *   Code begins at bottom of memory
 *   Stack begins at top of memory
 *   Dynamic heap can be put in-between
 * Registers:
 *   Register 0-9 - User Registers
 *   Register 10 - Flags: Z, C, S, I + 4 reserved
 *   Register 11 - FP: Frame Pointer
 *   Register 12 - SP: Stack Pointer
 *   Register 13 - IP: Instruction Pointer
 *   Register 14 - IV: Interrupt Vector
 *   Register 15 - IN: Interrupt Number
 * Stack Frame: (suggested, or enforced by ENTER opcode)
 *   Method Parameters (top of stack) [FP+13+arg#] arg=1, 2, ...
 *   Return IP (next instruction after CALL)
 *   Preserved Previous R0...R9
 *   Preserved Previous Flags
 *   Preserved Previous FP
 *   (Current FP points here)
 *   Local Variables (size=ENTER Arg1) [FP-local#] local=0, -1, ...
 *   (SP starts out here and grows downward)
 *   Temporaries
 * Calling Convention:    (Suggested)
 *   Push Params...
 *   CALLIMM xxxx
 *   ADDIMM SP, ParamsSize
 * Function Entry:        (Suggested, enforced by ENTER opcode)
 *   PUSHREG $0 ... PUSHREG $FP
 *   LOADREG $FP, $SP
 *   ADDIMM $SP, nnn      (Where nnn = LocalsSize)
 * Function Exit:         (Suggested, enforced by LEAVE opcode, see Note1) 
 *   LOADREG $SP, $FP
 *   POPREG $FP ... POPREG $0
 *   RET                  (see Note1)
 * *Note1: For an ISR, use ILEAVE/IRET instead of LEAVE/RET
 * FP offsets, assuming there are 2 one-byte parameters and 3 one-byte locals:
 *   PARAM_2    FP+2
 *   PARAM_1    FP+1
 *   ...
 *   LOCAL_0    FP
 *   LOCAL_1    FP-1
 *   LOCAL_2    FP-2
 * Class Hierarchies
 *   Substrate
 *     ExecutorNoArg
 *       ExecutorOneArg
 *         ExecutorTwoArgs
 *           CPU
 * BByte
 *   MemoryCell
 *   IOPort
 *   Adder
 *     Register
 *       FlagRegister

Opcode Mnemonic #Args
 * (0, "NOOP", 0),
 * (1, "INVALID", 0),
 * (2, "TRAP", 0),
 * (5, "RET", 0),
 * (6, "IRET", 0),
 * (8, "LEAVE", 0),
 * (9, "ILEAVE", 0),
 * (10, "ENTER", 1),
 * (11, "JMPREG", 1),
 * (14, "JMPIMM", 1),
 * (15, "JZEIMM", 1),
 * (16, "JGTIMM", 1),
 * (17, "JLTIMM", 1),
 * (18, "JNZEIMM", 1),
 * (19, "JGTEIMM", 1),
 * (20, "JLTEIMM", 1),
 * (30, "CALLIMM", 1),
 * (31, "PUSHREG", 1),
 * (32, "POPREG", 1),
 * (35, "NEGATE", 1),
 * (36, "INVERT", 1),
 * (38, "INCREG", 1),
 * (39, "DECREG", 1),
 * (50, "TSWAIT", 1 ),
 * (51, "ZEROREG", 1),
 * (52, "ZEROMEM", 1),
 * (100, "SHFLREG", 2),
 * (101, "SHFRREG", 2),
 * (102, "ANDREG", 2),
 * (103, "XORREG", 2),
 * (104, "ORREG", 2),
 * (111, "ADDIMM", 2),
 * (112, "ADDREG", 2),
 * (113, "ADDMEM", 2),
 * (114, "ADDFRA", 2),
 * (115, "ADDIND", 2),
 * (121, "ADCIMM", 2),
 * (122, "ADCREG", 2),
 * (131, "SUBIMM", 2),
 * (132, "SUBREG", 2),
 * (135, "CMPIMM", 2),
 * (136, "CMPREG", 2),
 * (141, "LOADIMM", 2),
 * (142, "LOADREG", 2),
 * (143, "LOADMEM", 2),
 * (144, "LOADFRA", 2),
 * (145, "LOADIND", 2),
 * (151, "STORMEM", 2),
 * (154, "STORFRA", 2),
 * (155, "STORIND", 2),
 * (201, "INPREG", 2),
 * (211, "OUTREG", 2);

Assembler Notes
 * Quick two-pass Assembler/LinkLoader.
 *   See AssemblerTest.java for examples.
 * Meaning of opcode Mnemonic:
 *   Operation[Condition][AddrMode]
 * Condition:
 *   ZE = Zero or Equal         NZE = Not Zero or Equal
 *   LT = Less than             LTE = Less Than or Equal
 *   GT = Greater than          GTE = Greater Than or Equal
 * AddrMode:
 *   IMM = Immediate value - value is Arg2 literally
 *   REG = Register number - designated register Arg2
 *   IND = Indirect - memory as specified by the designated register Arg2
 *   MEM = Memory address - memory as specified by Arg2
 *   FRA = Stack frame offset from FP (only after ENTER opcode) see Note3
 *     Note1: for 2 Arg Ops: Arg1 is always REG so the AddrMode is for Arg2
 *     Note2: AddrMode for source, except STORxxx where it's destination
 *     Note3: FRA AddrMode 1, 2, 3... for params; 0, -1, -2... for locals
 * Pass in a string that contains a series of the following statements:
 *  [label:] [address:] [[opcode[,]] [arg[,]...] [label:] [byte[,]...] ["string"]
 * Registers:
 *   $0 .. $9  User registers - General purpose accumulators/indexers
 *   $FLAG     Flag register - Bit Weights: 1=Zero, 2=Carry, 4=Sign, 8=EnabIntr
 *   $FP       Frame Pointer - Normally maintained by ENTER/LEAVE/ILEAVE opcodes
 *   $SP       Stack Pointer - Used by PUSHxxx/POPxxx,ENTER/LEAVE/ILEAVE opcodes
 *   $IP       Instruction Pointer - System Use ONLY
 *   $IV       Interrupt Vector - Set this to your ISR or ISR discriminator
 *   $IN       Interrupt Number - System sets this to the current IRQ
 *   $SC       Internal Scratch Register - Overwritten by arith/logic opcodes
 * These can be newline-delimited or semicolon-delimited.
 * - If the address is omitted, it increments starting from zero.
 * - Opcodes are not case-sensitive.
 * - Spaces are optional and ignored.
 * - Comments begin with # and continue to the end of line or a semicolon.
 * - If a label is left-justified then it's a target instead of a reference.
 * - ConstantArgs/Bytes can start with "0x" if they are hex instead of decimal.
 * - Register numbers start with "0$" or "$", required if it's a register name.
 * - Math/logic operations on System registers do not set ZE/GT/LT flags.
 * - There is a directive/pseudo-op EQU for creating numeric constants as well.
~~~



