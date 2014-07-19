package Util;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;


public class ClassificaFuzzy {
	
	public static void classificar(double nt1, double nt2){
		String filename = "alunos.fcl";
		FIS fis = FIS.load(filename, true);
		System.out.println("Foi chamada a classe");
		if (fis == null) {
			System.err.println("Can't load file: '" + filename + "'");
			System.exit(1);
		}

		// Get default function block
		FunctionBlock fb = fis.getFunctionBlock(null);

		// Set inputs
		fb.setVariable("nota1", nt1);
		fb.setVariable("nota2", nt2);

		// Evaluate
		fb.evaluate();

		// Show output variable's chart
		fb.getVariable("tipo").defuzzify();

		// Print ruleSet
		System.out.println(fb);
		System.out.println("Fuzzy Tipo: " + fb.getVariable("tipo").getValue());

	}
		
}
