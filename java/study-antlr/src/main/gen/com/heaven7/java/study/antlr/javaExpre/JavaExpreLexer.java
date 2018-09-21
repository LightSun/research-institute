// Generated from E:/study/github/research-institute/java/study-antlr/src/main/java/com/heaven7/java/study/antlr/javaExpre\JavaExpre.g4 by ANTLR 4.7
package com.heaven7.java.study.antlr.javaExpre;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class JavaExpreLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, ClassName=3, VarName=4, StringLiteral=5, IntNumber=6, 
		FloatNumber=7, NullLiteral=8, LPAREN=9, RPAREN=10, LBRACK=11, RBRACK=12, 
		COMMA=13, DOT=14, AND=15, OR=16, NOTEQUAL=17, EQUAL=18, QUESTION=19, COLON=20, 
		WS=21, COMMENT=22, LINE_COMMENT=23;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "ClassName", "VarName", "StringLiteral", "IntNumber", 
		"FloatNumber", "StringCharacters", "StringCharacter", "EscapeSequence", 
		"OctalEscape", "OctalDigit", "ZeroToThree", "NullLiteral", "LPAREN", "RPAREN", 
		"LBRACK", "RBRACK", "COMMA", "DOT", "JavaLetterOrDigit", "Digit", "AND", 
		"OR", "NOTEQUAL", "EQUAL", "QUESTION", "COLON", "WS", "COMMENT", "LINE_COMMENT"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'true'", "'false'", null, null, null, null, null, "'null'", "'('", 
		"')'", "'['", "']'", "','", "'.'", "'&&'", "'||'", "'!='", "'=='", "'?'", 
		"':'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, "ClassName", "VarName", "StringLiteral", "IntNumber", 
		"FloatNumber", "NullLiteral", "LPAREN", "RPAREN", "LBRACK", "RBRACK", 
		"COMMA", "DOT", "AND", "OR", "NOTEQUAL", "EQUAL", "QUESTION", "COLON", 
		"WS", "COMMENT", "LINE_COMMENT"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public JavaExpreLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "JavaExpre.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 20:
			return JavaLetterOrDigit_sempred((RuleContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean JavaLetterOrDigit_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return Character.isJavaIdentifierPart(_input.LA(-1));
		case 1:
			return Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)));
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\31\u00da\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\7\4O\n\4\f\4\16"+
		"\4R\13\4\3\5\3\5\7\5V\n\5\f\5\16\5Y\13\5\3\6\3\6\5\6]\n\6\3\6\3\6\3\7"+
		"\6\7b\n\7\r\7\16\7c\3\b\6\bg\n\b\r\b\16\bh\3\b\3\b\6\bm\n\b\r\b\16\bn"+
		"\3\t\6\tr\n\t\r\t\16\ts\3\n\3\n\5\nx\n\n\3\13\3\13\3\13\5\13}\n\13\3\f"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u008a\n\f\3\r\3\r\3\16\3"+
		"\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3"+
		"\24\3\24\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\5\26\u00a7\n\26\3\27"+
		"\3\27\3\30\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\32\3\33\3\33\3\33\3\34"+
		"\3\34\3\35\3\35\3\36\6\36\u00bc\n\36\r\36\16\36\u00bd\3\36\3\36\3\37\3"+
		"\37\3\37\3\37\7\37\u00c6\n\37\f\37\16\37\u00c9\13\37\3\37\3\37\3\37\3"+
		"\37\3\37\3 \3 \3 \3 \7 \u00d4\n \f \16 \u00d7\13 \3 \3 \3\u00c7\2!\3\3"+
		"\5\4\7\5\t\6\13\7\r\b\17\t\21\2\23\2\25\2\27\2\31\2\33\2\35\n\37\13!\f"+
		"#\r%\16\'\17)\20+\2-\2/\21\61\22\63\23\65\24\67\259\26;\27=\30?\31\3\2"+
		"\17\3\2C\\\5\2&&aac|\4\2$$^^\n\2$$))^^ddhhppttvv\3\2\629\3\2\62\65\7\2"+
		"&&\62;C\\aac|\4\2\2\u0101\ud802\udc01\3\2\ud802\udc01\3\2\udc02\ue001"+
		"\3\2\62;\5\2\13\f\16\17\"\"\4\2\f\f\17\17\2\u00e1\2\3\3\2\2\2\2\5\3\2"+
		"\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\35"+
		"\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)"+
		"\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2"+
		"\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\3A\3\2\2\2\5F\3\2\2\2\7"+
		"L\3\2\2\2\tS\3\2\2\2\13Z\3\2\2\2\ra\3\2\2\2\17f\3\2\2\2\21q\3\2\2\2\23"+
		"w\3\2\2\2\25|\3\2\2\2\27\u0089\3\2\2\2\31\u008b\3\2\2\2\33\u008d\3\2\2"+
		"\2\35\u008f\3\2\2\2\37\u0094\3\2\2\2!\u0096\3\2\2\2#\u0098\3\2\2\2%\u009a"+
		"\3\2\2\2\'\u009c\3\2\2\2)\u009e\3\2\2\2+\u00a6\3\2\2\2-\u00a8\3\2\2\2"+
		"/\u00aa\3\2\2\2\61\u00ad\3\2\2\2\63\u00b0\3\2\2\2\65\u00b3\3\2\2\2\67"+
		"\u00b6\3\2\2\29\u00b8\3\2\2\2;\u00bb\3\2\2\2=\u00c1\3\2\2\2?\u00cf\3\2"+
		"\2\2AB\7v\2\2BC\7t\2\2CD\7w\2\2DE\7g\2\2E\4\3\2\2\2FG\7h\2\2GH\7c\2\2"+
		"HI\7n\2\2IJ\7u\2\2JK\7g\2\2K\6\3\2\2\2LP\t\2\2\2MO\5+\26\2NM\3\2\2\2O"+
		"R\3\2\2\2PN\3\2\2\2PQ\3\2\2\2Q\b\3\2\2\2RP\3\2\2\2SW\t\3\2\2TV\5+\26\2"+
		"UT\3\2\2\2VY\3\2\2\2WU\3\2\2\2WX\3\2\2\2X\n\3\2\2\2YW\3\2\2\2Z\\\7$\2"+
		"\2[]\5\21\t\2\\[\3\2\2\2\\]\3\2\2\2]^\3\2\2\2^_\7$\2\2_\f\3\2\2\2`b\5"+
		"-\27\2a`\3\2\2\2bc\3\2\2\2ca\3\2\2\2cd\3\2\2\2d\16\3\2\2\2eg\5-\27\2f"+
		"e\3\2\2\2gh\3\2\2\2hf\3\2\2\2hi\3\2\2\2ij\3\2\2\2jl\5)\25\2km\5-\27\2"+
		"lk\3\2\2\2mn\3\2\2\2nl\3\2\2\2no\3\2\2\2o\20\3\2\2\2pr\5\23\n\2qp\3\2"+
		"\2\2rs\3\2\2\2sq\3\2\2\2st\3\2\2\2t\22\3\2\2\2ux\n\4\2\2vx\5\25\13\2w"+
		"u\3\2\2\2wv\3\2\2\2x\24\3\2\2\2yz\7^\2\2z}\t\5\2\2{}\5\27\f\2|y\3\2\2"+
		"\2|{\3\2\2\2}\26\3\2\2\2~\177\7^\2\2\177\u008a\5\31\r\2\u0080\u0081\7"+
		"^\2\2\u0081\u0082\5\31\r\2\u0082\u0083\5\31\r\2\u0083\u008a\3\2\2\2\u0084"+
		"\u0085\7^\2\2\u0085\u0086\5\33\16\2\u0086\u0087\5\31\r\2\u0087\u0088\5"+
		"\31\r\2\u0088\u008a\3\2\2\2\u0089~\3\2\2\2\u0089\u0080\3\2\2\2\u0089\u0084"+
		"\3\2\2\2\u008a\30\3\2\2\2\u008b\u008c\t\6\2\2\u008c\32\3\2\2\2\u008d\u008e"+
		"\t\7\2\2\u008e\34\3\2\2\2\u008f\u0090\7p\2\2\u0090\u0091\7w\2\2\u0091"+
		"\u0092\7n\2\2\u0092\u0093\7n\2\2\u0093\36\3\2\2\2\u0094\u0095\7*\2\2\u0095"+
		" \3\2\2\2\u0096\u0097\7+\2\2\u0097\"\3\2\2\2\u0098\u0099\7]\2\2\u0099"+
		"$\3\2\2\2\u009a\u009b\7_\2\2\u009b&\3\2\2\2\u009c\u009d\7.\2\2\u009d("+
		"\3\2\2\2\u009e\u009f\7\60\2\2\u009f*\3\2\2\2\u00a0\u00a7\t\b\2\2\u00a1"+
		"\u00a2\n\t\2\2\u00a2\u00a7\6\26\2\2\u00a3\u00a4\t\n\2\2\u00a4\u00a5\t"+
		"\13\2\2\u00a5\u00a7\6\26\3\2\u00a6\u00a0\3\2\2\2\u00a6\u00a1\3\2\2\2\u00a6"+
		"\u00a3\3\2\2\2\u00a7,\3\2\2\2\u00a8\u00a9\t\f\2\2\u00a9.\3\2\2\2\u00aa"+
		"\u00ab\7(\2\2\u00ab\u00ac\7(\2\2\u00ac\60\3\2\2\2\u00ad\u00ae\7~\2\2\u00ae"+
		"\u00af\7~\2\2\u00af\62\3\2\2\2\u00b0\u00b1\7#\2\2\u00b1\u00b2\7?\2\2\u00b2"+
		"\64\3\2\2\2\u00b3\u00b4\7?\2\2\u00b4\u00b5\7?\2\2\u00b5\66\3\2\2\2\u00b6"+
		"\u00b7\7A\2\2\u00b78\3\2\2\2\u00b8\u00b9\7<\2\2\u00b9:\3\2\2\2\u00ba\u00bc"+
		"\t\r\2\2\u00bb\u00ba\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bd\u00bb\3\2\2\2\u00bd"+
		"\u00be\3\2\2\2\u00be\u00bf\3\2\2\2\u00bf\u00c0\b\36\2\2\u00c0<\3\2\2\2"+
		"\u00c1\u00c2\7\61\2\2\u00c2\u00c3\7,\2\2\u00c3\u00c7\3\2\2\2\u00c4\u00c6"+
		"\13\2\2\2\u00c5\u00c4\3\2\2\2\u00c6\u00c9\3\2\2\2\u00c7\u00c8\3\2\2\2"+
		"\u00c7\u00c5\3\2\2\2\u00c8\u00ca\3\2\2\2\u00c9\u00c7\3\2\2\2\u00ca\u00cb"+
		"\7,\2\2\u00cb\u00cc\7\61\2\2\u00cc\u00cd\3\2\2\2\u00cd\u00ce\b\37\2\2"+
		"\u00ce>\3\2\2\2\u00cf\u00d0\7\61\2\2\u00d0\u00d1\7\61\2\2\u00d1\u00d5"+
		"\3\2\2\2\u00d2\u00d4\n\16\2\2\u00d3\u00d2\3\2\2\2\u00d4\u00d7\3\2\2\2"+
		"\u00d5\u00d3\3\2\2\2\u00d5\u00d6\3\2\2\2\u00d6\u00d8\3\2\2\2\u00d7\u00d5"+
		"\3\2\2\2\u00d8\u00d9\b \2\2\u00d9@\3\2\2\2\21\2PW\\chnsw|\u0089\u00a6"+
		"\u00bd\u00c7\u00d5\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}