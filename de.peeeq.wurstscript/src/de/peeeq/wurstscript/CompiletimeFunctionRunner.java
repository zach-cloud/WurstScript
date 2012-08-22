package de.peeeq.wurstscript;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.peeeq.wurstscript.ast.AstElement;
import de.peeeq.wurstscript.attributes.CompileError;
import de.peeeq.wurstscript.gui.WurstGui;
import de.peeeq.wurstscript.intermediateLang.interpreter.ILInterpreter;
import de.peeeq.wurstscript.jassIm.ImFunction;
import de.peeeq.wurstscript.jassIm.ImProg;
import de.peeeq.wurstscript.jassIm.ImStmt;
import de.peeeq.wurstscript.jassinterpreter.TestFailException;
import de.peeeq.wurstscript.jassinterpreter.TestSuccessException;
import de.peeeq.wurstscript.translation.imtranslation.FunctionFlag;
import de.peeeq.wurstscript.utils.Pair;

public class CompiletimeFunctionRunner {

	private final ImProg imProg;
	private final File mapFile;
	private ILInterpreter interpreter;
	private WurstGui gui;
	private FunctionFlag functionFlag;
	private List<ImFunction> successTests = Lists.newArrayList();
	private Map<ImFunction, Pair<ImStmt, String>> failTests = Maps.newHashMap();

	


	public CompiletimeFunctionRunner(ImProg imProg, File mapFile, WurstGui gui, FunctionFlag flag) {
		this.imProg = imProg;
		this.mapFile = mapFile;
		this.interpreter = new ILInterpreter(imProg, gui, mapFile);
		this.gui = gui;
		this.functionFlag = flag;
	}
	

	public void run() {
		gui.sendProgress("Running compiletime functions", 0.9);
//		interpreter.executeFunction("main");
//		interpreter.executeFunction("initGlobals");
		try {
			for (ImFunction f : imProg.getFunctions()) {
				if (f.hasFlag(functionFlag)) {
					try {
						interpreter.runVoidFunc(f);
						successTests.add(f);
					} catch (TestSuccessException e) {
						successTests.add(f);
					} catch (TestFailException e) {
						failTests.put(f, Pair.create(interpreter.getLastStatement(), e.toString()));
					}
				}
			}
			
			interpreter.writebackGlobalState();
		} catch (Throwable e) {
			WLogger.severe(e);
			ImStmt s = interpreter.getLastStatement();
			AstElement origin = s.attrTrace();
			if (origin != null) { 
				gui.sendError(new CompileError(origin.attrSource(), e.getMessage()));
			} else {
				throw new Error("could not get origin");
			}
		}
		
	}

	public List<ImFunction> getSuccessTests() {
		return successTests;
	}


	public Map<ImFunction, Pair<ImStmt, String>> getFailTests() {
		return failTests;
	}

}
