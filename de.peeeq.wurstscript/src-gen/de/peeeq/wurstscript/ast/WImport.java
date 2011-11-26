//generated by parseq
package de.peeeq.wurstscript.ast;

public interface WImport extends AstElement {
	AstElement getParent();
	void setSource(WPos source);
	WPos getSource();
	void setPackagename(String packagename);
	String getPackagename();
	WImport copy();
	public abstract void accept(WImport.Visitor v);
	public abstract void accept(TopLevelDeclaration.Visitor v);
	public abstract void accept(WPackage.Visitor v);
	public abstract void accept(PackageOrGlobal.Visitor v);
	public abstract void accept(CompilationUnit.Visitor v);
	public abstract void accept(WScope.Visitor v);
	public abstract void accept(WImports.Visitor v);
	public interface Visitor {
		void visit(WPos wPos);
		void visit(WImport wImport);
	}
	public static abstract class DefaultVisitor implements Visitor {
		@Override public void visit(WPos wPos) {}
		@Override public void visit(WImport wImport) {}
	}
}
