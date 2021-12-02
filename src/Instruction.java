public class Instruction extends BaseInstruction {
	String label;
	String assemblyLine;


	public Instruction(BaseInstruction baseInstruction, String assemblyLine) {
		super(baseInstruction.opcode, baseInstruction.name, baseInstruction.format);
		this.assemblyLine = assemblyLine.trim();
	}

	public void addLabel(String label){
		this.label = label;
	}
	public boolean hasLabel(){
		return label != null && !label.isEmpty();
	}

	@Override
	public String toString() {
		if (!hasLabel()) return assemblyLine;
		return label + ": " + assemblyLine;
	}
}