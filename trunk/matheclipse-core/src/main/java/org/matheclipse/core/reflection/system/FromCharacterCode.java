package org.matheclipse.core.reflection.system;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.StringX;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;

public class FromCharacterCode extends AbstractFunctionEvaluator {

	public FromCharacterCode() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if (ast.size() != 2) {
			return null;
		}

		if (ast.get(1).isList()) {
			final IAST list = (IAST) ast.get(1);
			final StringBuffer buffer = new StringBuffer();
			char ch;
			for (int i = 1; i < list.size(); i++) {
				if (list.get(i).isInteger()) {
					ch = (char) Validate.checkIntType(list, i);
					buffer.append(ch);
				} else {
					return null;
				}
			}
			return StringX.valueOf(buffer);
		}
		if (ast.get(1).isInteger()) {
			final char ch = (char) Validate.checkIntType(ast, 1);
			return StringX.valueOf(ch);
		}

		return null;
	}

	@Override
	public void setUp(final ISymbol symbol) {
	}

	public static List<IExpr> fromCharcterCode(final String unicodeInput, final String inputEncoding, final List<IExpr> list) {
		try {
			final String utf8String = new String(unicodeInput.getBytes(inputEncoding), "UTF-8");
			int characterCode;
			for (int i = 0; i < utf8String.length(); i++) {
				characterCode = utf8String.charAt(i);
				list.add(F.integer(characterCode));
			}
			return list;
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
