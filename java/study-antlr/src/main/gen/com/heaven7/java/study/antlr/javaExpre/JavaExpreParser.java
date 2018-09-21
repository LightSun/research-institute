// Generated from E:/study/github/research-institute/java/study-antlr/src/main/java/com/heaven7/java/study/antlr/javaExpre\JavaExpre.g4 by ANTLR 4.7
package com.heaven7.java.study.antlr.javaExpre;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class JavaExpreParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, ClassName=3, VarName=4, StringLiteral=5, IntNumber=6, 
		FloatNumber=7, NullLiteral=8, LPAREN=9, RPAREN=10, LBRACK=11, RBRACK=12, 
		COMMA=13, DOT=14, AND=15, OR=16, NOTEQUAL=17, EQUAL=18, QUESTION=19, COLON=20, 
		WS=21, COMMENT=22, LINE_COMMENT=23;
	public static final int
		RULE_surroundSimpleExpre = 0, RULE_simpleExpre = 1, RULE_surroundExpre = 2, 
		RULE_expre = 3, RULE_simpleExpreList = 4, RULE_arrayInvoke = 5, RULE_fieldInvoke = 6, 
		RULE_methodInvoke = 7, RULE_arrayExpre = 8, RULE_fieldExpre = 9, RULE_methodExpre = 10, 
		RULE_ternaryExpre = 11, RULE_surroundTernaryExpre = 12, RULE_doubleOrExpre = 13, 
		RULE_surroundDoubleOrExpre = 14, RULE_doubeAndExpre = 15, RULE_surroundDoubeAndExpre = 16, 
		RULE_equalExpre = 17, RULE_surroundEqualExpre = 18, RULE_notEqualExpre = 19, 
		RULE_surroundNotEqualExpre = 20, RULE_booleanConstantExpre = 21, RULE_classNameExpre = 22, 
		RULE_varNameExpre = 23;
	public static final String[] ruleNames = {
		"surroundSimpleExpre", "simpleExpre", "surroundExpre", "expre", "simpleExpreList", 
		"arrayInvoke", "fieldInvoke", "methodInvoke", "arrayExpre", "fieldExpre", 
		"methodExpre", "ternaryExpre", "surroundTernaryExpre", "doubleOrExpre", 
		"surroundDoubleOrExpre", "doubeAndExpre", "surroundDoubeAndExpre", "equalExpre", 
		"surroundEqualExpre", "notEqualExpre", "surroundNotEqualExpre", "booleanConstantExpre", 
		"classNameExpre", "varNameExpre"
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

	@Override
	public String getGrammarFileName() { return "JavaExpre.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public JavaExpreParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class SurroundSimpleExpreContext extends ParserRuleContext {
		public SimpleExpreContext simpleExpre() {
			return getRuleContext(SimpleExpreContext.class,0);
		}
		public SurroundSimpleExpreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_surroundSimpleExpre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterSurroundSimpleExpre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitSurroundSimpleExpre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitSurroundSimpleExpre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SurroundSimpleExpreContext surroundSimpleExpre() throws RecognitionException {
		SurroundSimpleExpreContext _localctx = new SurroundSimpleExpreContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_surroundSimpleExpre);
		try {
			setState(53);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(48);
				simpleExpre();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(49);
				match(LPAREN);
				setState(50);
				simpleExpre();
				setState(51);
				match(RPAREN);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SimpleExpreContext extends ParserRuleContext {
		public TernaryExpreContext ternaryExpre() {
			return getRuleContext(TernaryExpreContext.class,0);
		}
		public ExpreContext expre() {
			return getRuleContext(ExpreContext.class,0);
		}
		public DoubeAndExpreContext doubeAndExpre() {
			return getRuleContext(DoubeAndExpreContext.class,0);
		}
		public DoubleOrExpreContext doubleOrExpre() {
			return getRuleContext(DoubleOrExpreContext.class,0);
		}
		public EqualExpreContext equalExpre() {
			return getRuleContext(EqualExpreContext.class,0);
		}
		public NotEqualExpreContext notEqualExpre() {
			return getRuleContext(NotEqualExpreContext.class,0);
		}
		public SimpleExpreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleExpre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterSimpleExpre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitSimpleExpre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitSimpleExpre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleExpreContext simpleExpre() throws RecognitionException {
		SimpleExpreContext _localctx = new SimpleExpreContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_simpleExpre);
		try {
			setState(61);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(55);
				ternaryExpre();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(56);
				expre();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(57);
				doubeAndExpre();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(58);
				doubleOrExpre();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(59);
				equalExpre();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(60);
				notEqualExpre();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SurroundExpreContext extends ParserRuleContext {
		public ExpreContext expre() {
			return getRuleContext(ExpreContext.class,0);
		}
		public SurroundExpreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_surroundExpre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterSurroundExpre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitSurroundExpre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitSurroundExpre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SurroundExpreContext surroundExpre() throws RecognitionException {
		SurroundExpreContext _localctx = new SurroundExpreContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_surroundExpre);
		try {
			setState(68);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
			case T__1:
			case ClassName:
			case VarName:
			case StringLiteral:
			case IntNumber:
			case FloatNumber:
				enterOuterAlt(_localctx, 1);
				{
				setState(63);
				expre();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(64);
				match(LPAREN);
				setState(65);
				expre();
				setState(66);
				match(RPAREN);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpreContext extends ParserRuleContext {
		public VarNameExpreContext varNameExpre() {
			return getRuleContext(VarNameExpreContext.class,0);
		}
		public ArrayInvokeContext arrayInvoke() {
			return getRuleContext(ArrayInvokeContext.class,0);
		}
		public FieldInvokeContext fieldInvoke() {
			return getRuleContext(FieldInvokeContext.class,0);
		}
		public MethodInvokeContext methodInvoke() {
			return getRuleContext(MethodInvokeContext.class,0);
		}
		public TerminalNode StringLiteral() { return getToken(JavaExpreParser.StringLiteral, 0); }
		public TerminalNode IntNumber() { return getToken(JavaExpreParser.IntNumber, 0); }
		public TerminalNode FloatNumber() { return getToken(JavaExpreParser.FloatNumber, 0); }
		public BooleanConstantExpreContext booleanConstantExpre() {
			return getRuleContext(BooleanConstantExpreContext.class,0);
		}
		public ExpreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterExpre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitExpre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitExpre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpreContext expre() throws RecognitionException {
		ExpreContext _localctx = new ExpreContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_expre);
		try {
			setState(78);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(70);
				varNameExpre();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(71);
				arrayInvoke();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(72);
				fieldInvoke();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(73);
				methodInvoke();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(74);
				match(StringLiteral);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(75);
				match(IntNumber);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(76);
				match(FloatNumber);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(77);
				booleanConstantExpre();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SimpleExpreListContext extends ParserRuleContext {
		public List<SurroundSimpleExpreContext> surroundSimpleExpre() {
			return getRuleContexts(SurroundSimpleExpreContext.class);
		}
		public SurroundSimpleExpreContext surroundSimpleExpre(int i) {
			return getRuleContext(SurroundSimpleExpreContext.class,i);
		}
		public SimpleExpreListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleExpreList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterSimpleExpreList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitSimpleExpreList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitSimpleExpreList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleExpreListContext simpleExpreList() throws RecognitionException {
		SimpleExpreListContext _localctx = new SimpleExpreListContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_simpleExpreList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			surroundSimpleExpre();
			setState(85);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(81);
				match(COMMA);
				setState(82);
				surroundSimpleExpre();
				}
				}
				setState(87);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayInvokeContext extends ParserRuleContext {
		public VarNameExpreContext varNameExpre() {
			return getRuleContext(VarNameExpreContext.class,0);
		}
		public ArrayExpreContext arrayExpre() {
			return getRuleContext(ArrayExpreContext.class,0);
		}
		public ArrayInvokeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayInvoke; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterArrayInvoke(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitArrayInvoke(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitArrayInvoke(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayInvokeContext arrayInvoke() throws RecognitionException {
		ArrayInvokeContext _localctx = new ArrayInvokeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_arrayInvoke);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			varNameExpre();
			setState(89);
			arrayExpre();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldInvokeContext extends ParserRuleContext {
		public FieldExpreContext fieldExpre() {
			return getRuleContext(FieldExpreContext.class,0);
		}
		public ArrayExpreContext arrayExpre() {
			return getRuleContext(ArrayExpreContext.class,0);
		}
		public TerminalNode DOT() { return getToken(JavaExpreParser.DOT, 0); }
		public MethodExpreContext methodExpre() {
			return getRuleContext(MethodExpreContext.class,0);
		}
		public FieldInvokeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldInvoke; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterFieldInvoke(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitFieldInvoke(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitFieldInvoke(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldInvokeContext fieldInvoke() throws RecognitionException {
		FieldInvokeContext _localctx = new FieldInvokeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_fieldInvoke);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(91);
			fieldExpre();
			setState(95);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DOT:
				{
				{
				setState(92);
				match(DOT);
				setState(93);
				methodExpre();
				}
				}
				break;
			case LBRACK:
				{
				setState(94);
				arrayExpre();
				}
				break;
			case RPAREN:
			case RBRACK:
			case COMMA:
			case AND:
			case OR:
			case NOTEQUAL:
			case EQUAL:
			case QUESTION:
			case COLON:
				break;
			default:
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodInvokeContext extends ParserRuleContext {
		public ClassNameExpreContext classNameExpre() {
			return getRuleContext(ClassNameExpreContext.class,0);
		}
		public VarNameExpreContext varNameExpre() {
			return getRuleContext(VarNameExpreContext.class,0);
		}
		public List<TerminalNode> DOT() { return getTokens(JavaExpreParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(JavaExpreParser.DOT, i);
		}
		public List<MethodExpreContext> methodExpre() {
			return getRuleContexts(MethodExpreContext.class);
		}
		public MethodExpreContext methodExpre(int i) {
			return getRuleContext(MethodExpreContext.class,i);
		}
		public MethodInvokeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodInvoke; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterMethodInvoke(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitMethodInvoke(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitMethodInvoke(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodInvokeContext methodInvoke() throws RecognitionException {
		MethodInvokeContext _localctx = new MethodInvokeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_methodInvoke);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(99);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ClassName:
				{
				setState(97);
				classNameExpre();
				}
				break;
			case VarName:
				{
				setState(98);
				varNameExpre();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(103); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(101);
				match(DOT);
				setState(102);
				methodExpre();
				}
				}
				setState(105); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==DOT );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayExpreContext extends ParserRuleContext {
		public TerminalNode LBRACK() { return getToken(JavaExpreParser.LBRACK, 0); }
		public SurroundSimpleExpreContext surroundSimpleExpre() {
			return getRuleContext(SurroundSimpleExpreContext.class,0);
		}
		public TerminalNode RBRACK() { return getToken(JavaExpreParser.RBRACK, 0); }
		public ArrayExpreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayExpre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterArrayExpre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitArrayExpre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitArrayExpre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayExpreContext arrayExpre() throws RecognitionException {
		ArrayExpreContext _localctx = new ArrayExpreContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_arrayExpre);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			match(LBRACK);
			setState(108);
			surroundSimpleExpre();
			setState(109);
			match(RBRACK);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldExpreContext extends ParserRuleContext {
		public List<VarNameExpreContext> varNameExpre() {
			return getRuleContexts(VarNameExpreContext.class);
		}
		public VarNameExpreContext varNameExpre(int i) {
			return getRuleContext(VarNameExpreContext.class,i);
		}
		public ClassNameExpreContext classNameExpre() {
			return getRuleContext(ClassNameExpreContext.class,0);
		}
		public List<TerminalNode> DOT() { return getTokens(JavaExpreParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(JavaExpreParser.DOT, i);
		}
		public FieldExpreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldExpre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterFieldExpre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitFieldExpre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitFieldExpre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldExpreContext fieldExpre() throws RecognitionException {
		FieldExpreContext _localctx = new FieldExpreContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_fieldExpre);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(114);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ClassName) {
				{
				setState(111);
				classNameExpre();
				setState(112);
				match(DOT);
				}
			}

			setState(116);
			varNameExpre();
			setState(121);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(117);
					match(DOT);
					setState(118);
					varNameExpre();
					}
					} 
				}
				setState(123);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodExpreContext extends ParserRuleContext {
		public VarNameExpreContext varNameExpre() {
			return getRuleContext(VarNameExpreContext.class,0);
		}
		public SimpleExpreListContext simpleExpreList() {
			return getRuleContext(SimpleExpreListContext.class,0);
		}
		public MethodExpreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodExpre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterMethodExpre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitMethodExpre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitMethodExpre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodExpreContext methodExpre() throws RecognitionException {
		MethodExpreContext _localctx = new MethodExpreContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_methodExpre);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
			varNameExpre();
			setState(131);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(125);
				match(LPAREN);
				setState(126);
				match(RPAREN);
				}
				break;
			case 2:
				{
				{
				setState(127);
				match(LPAREN);
				setState(128);
				simpleExpreList();
				setState(129);
				match(RPAREN);
				}
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TernaryExpreContext extends ParserRuleContext {
		public SurroundDoubleOrExpreContext surroundDoubleOrExpre() {
			return getRuleContext(SurroundDoubleOrExpreContext.class,0);
		}
		public TerminalNode QUESTION() { return getToken(JavaExpreParser.QUESTION, 0); }
		public SurroundExpreContext surroundExpre() {
			return getRuleContext(SurroundExpreContext.class,0);
		}
		public TerminalNode COLON() { return getToken(JavaExpreParser.COLON, 0); }
		public SurroundTernaryExpreContext surroundTernaryExpre() {
			return getRuleContext(SurroundTernaryExpreContext.class,0);
		}
		public TernaryExpreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ternaryExpre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterTernaryExpre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitTernaryExpre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitTernaryExpre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TernaryExpreContext ternaryExpre() throws RecognitionException {
		TernaryExpreContext _localctx = new TernaryExpreContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_ternaryExpre);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(133);
			surroundDoubleOrExpre();
			setState(139);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==QUESTION) {
				{
				setState(134);
				match(QUESTION);
				setState(135);
				surroundExpre();
				setState(136);
				match(COLON);
				setState(137);
				surroundTernaryExpre();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SurroundTernaryExpreContext extends ParserRuleContext {
		public TernaryExpreContext ternaryExpre() {
			return getRuleContext(TernaryExpreContext.class,0);
		}
		public SurroundTernaryExpreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_surroundTernaryExpre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterSurroundTernaryExpre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitSurroundTernaryExpre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitSurroundTernaryExpre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SurroundTernaryExpreContext surroundTernaryExpre() throws RecognitionException {
		SurroundTernaryExpreContext _localctx = new SurroundTernaryExpreContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_surroundTernaryExpre);
		try {
			setState(146);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(141);
				ternaryExpre();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(142);
				match(LPAREN);
				setState(143);
				ternaryExpre();
				setState(144);
				match(RPAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DoubleOrExpreContext extends ParserRuleContext {
		public List<SurroundDoubeAndExpreContext> surroundDoubeAndExpre() {
			return getRuleContexts(SurroundDoubeAndExpreContext.class);
		}
		public SurroundDoubeAndExpreContext surroundDoubeAndExpre(int i) {
			return getRuleContext(SurroundDoubeAndExpreContext.class,i);
		}
		public DoubleOrExpreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_doubleOrExpre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterDoubleOrExpre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitDoubleOrExpre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitDoubleOrExpre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DoubleOrExpreContext doubleOrExpre() throws RecognitionException {
		DoubleOrExpreContext _localctx = new DoubleOrExpreContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_doubleOrExpre);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			surroundDoubeAndExpre();
			setState(153);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OR) {
				{
				{
				setState(149);
				match(OR);
				setState(150);
				surroundDoubeAndExpre();
				}
				}
				setState(155);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SurroundDoubleOrExpreContext extends ParserRuleContext {
		public DoubleOrExpreContext doubleOrExpre() {
			return getRuleContext(DoubleOrExpreContext.class,0);
		}
		public SurroundDoubleOrExpreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_surroundDoubleOrExpre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterSurroundDoubleOrExpre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitSurroundDoubleOrExpre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitSurroundDoubleOrExpre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SurroundDoubleOrExpreContext surroundDoubleOrExpre() throws RecognitionException {
		SurroundDoubleOrExpreContext _localctx = new SurroundDoubleOrExpreContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_surroundDoubleOrExpre);
		try {
			setState(161);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(156);
				doubleOrExpre();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(157);
				match(LPAREN);
				setState(158);
				doubleOrExpre();
				setState(159);
				match(RPAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DoubeAndExpreContext extends ParserRuleContext {
		public List<SurroundExpreContext> surroundExpre() {
			return getRuleContexts(SurroundExpreContext.class);
		}
		public SurroundExpreContext surroundExpre(int i) {
			return getRuleContext(SurroundExpreContext.class,i);
		}
		public List<SurroundEqualExpreContext> surroundEqualExpre() {
			return getRuleContexts(SurroundEqualExpreContext.class);
		}
		public SurroundEqualExpreContext surroundEqualExpre(int i) {
			return getRuleContext(SurroundEqualExpreContext.class,i);
		}
		public List<SurroundNotEqualExpreContext> surroundNotEqualExpre() {
			return getRuleContexts(SurroundNotEqualExpreContext.class);
		}
		public SurroundNotEqualExpreContext surroundNotEqualExpre(int i) {
			return getRuleContext(SurroundNotEqualExpreContext.class,i);
		}
		public DoubeAndExpreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_doubeAndExpre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterDoubeAndExpre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitDoubeAndExpre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitDoubeAndExpre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DoubeAndExpreContext doubeAndExpre() throws RecognitionException {
		DoubeAndExpreContext _localctx = new DoubeAndExpreContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_doubeAndExpre);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(166);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(163);
				surroundExpre();
				}
				break;
			case 2:
				{
				setState(164);
				surroundEqualExpre();
				}
				break;
			case 3:
				{
				setState(165);
				surroundNotEqualExpre();
				}
				break;
			}
			setState(176);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND) {
				{
				{
				setState(168);
				match(AND);
				setState(172);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
				case 1:
					{
					setState(169);
					surroundExpre();
					}
					break;
				case 2:
					{
					setState(170);
					surroundEqualExpre();
					}
					break;
				case 3:
					{
					setState(171);
					surroundNotEqualExpre();
					}
					break;
				}
				}
				}
				setState(178);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SurroundDoubeAndExpreContext extends ParserRuleContext {
		public DoubeAndExpreContext doubeAndExpre() {
			return getRuleContext(DoubeAndExpreContext.class,0);
		}
		public SurroundDoubeAndExpreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_surroundDoubeAndExpre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterSurroundDoubeAndExpre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitSurroundDoubeAndExpre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitSurroundDoubeAndExpre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SurroundDoubeAndExpreContext surroundDoubeAndExpre() throws RecognitionException {
		SurroundDoubeAndExpreContext _localctx = new SurroundDoubeAndExpreContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_surroundDoubeAndExpre);
		try {
			setState(184);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(179);
				doubeAndExpre();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(180);
				match(LPAREN);
				setState(181);
				doubeAndExpre();
				setState(182);
				match(RPAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EqualExpreContext extends ParserRuleContext {
		public List<SurroundExpreContext> surroundExpre() {
			return getRuleContexts(SurroundExpreContext.class);
		}
		public SurroundExpreContext surroundExpre(int i) {
			return getRuleContext(SurroundExpreContext.class,i);
		}
		public EqualExpreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equalExpre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterEqualExpre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitEqualExpre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitEqualExpre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EqualExpreContext equalExpre() throws RecognitionException {
		EqualExpreContext _localctx = new EqualExpreContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_equalExpre);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186);
			surroundExpre();
			setState(187);
			match(EQUAL);
			setState(188);
			surroundExpre();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SurroundEqualExpreContext extends ParserRuleContext {
		public EqualExpreContext equalExpre() {
			return getRuleContext(EqualExpreContext.class,0);
		}
		public SurroundEqualExpreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_surroundEqualExpre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterSurroundEqualExpre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitSurroundEqualExpre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitSurroundEqualExpre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SurroundEqualExpreContext surroundEqualExpre() throws RecognitionException {
		SurroundEqualExpreContext _localctx = new SurroundEqualExpreContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_surroundEqualExpre);
		try {
			setState(195);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(190);
				equalExpre();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(191);
				match(LPAREN);
				setState(192);
				equalExpre();
				setState(193);
				match(RPAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NotEqualExpreContext extends ParserRuleContext {
		public List<SurroundExpreContext> surroundExpre() {
			return getRuleContexts(SurroundExpreContext.class);
		}
		public SurroundExpreContext surroundExpre(int i) {
			return getRuleContext(SurroundExpreContext.class,i);
		}
		public NotEqualExpreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notEqualExpre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterNotEqualExpre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitNotEqualExpre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitNotEqualExpre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NotEqualExpreContext notEqualExpre() throws RecognitionException {
		NotEqualExpreContext _localctx = new NotEqualExpreContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_notEqualExpre);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(197);
			surroundExpre();
			setState(198);
			match(NOTEQUAL);
			setState(199);
			surroundExpre();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SurroundNotEqualExpreContext extends ParserRuleContext {
		public NotEqualExpreContext notEqualExpre() {
			return getRuleContext(NotEqualExpreContext.class,0);
		}
		public SurroundNotEqualExpreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_surroundNotEqualExpre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterSurroundNotEqualExpre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitSurroundNotEqualExpre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitSurroundNotEqualExpre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SurroundNotEqualExpreContext surroundNotEqualExpre() throws RecognitionException {
		SurroundNotEqualExpreContext _localctx = new SurroundNotEqualExpreContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_surroundNotEqualExpre);
		try {
			setState(206);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(201);
				notEqualExpre();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(202);
				match(LPAREN);
				setState(203);
				notEqualExpre();
				setState(204);
				match(RPAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BooleanConstantExpreContext extends ParserRuleContext {
		public BooleanConstantExpreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanConstantExpre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterBooleanConstantExpre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitBooleanConstantExpre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitBooleanConstantExpre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanConstantExpreContext booleanConstantExpre() throws RecognitionException {
		BooleanConstantExpreContext _localctx = new BooleanConstantExpreContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_booleanConstantExpre);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(208);
			_la = _input.LA(1);
			if ( !(_la==T__0 || _la==T__1) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassNameExpreContext extends ParserRuleContext {
		public TerminalNode ClassName() { return getToken(JavaExpreParser.ClassName, 0); }
		public ClassNameExpreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classNameExpre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterClassNameExpre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitClassNameExpre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitClassNameExpre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassNameExpreContext classNameExpre() throws RecognitionException {
		ClassNameExpreContext _localctx = new ClassNameExpreContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_classNameExpre);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			match(ClassName);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarNameExpreContext extends ParserRuleContext {
		public TerminalNode VarName() { return getToken(JavaExpreParser.VarName, 0); }
		public VarNameExpreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varNameExpre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).enterVarNameExpre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaExpreListener ) ((JavaExpreListener)listener).exitVarNameExpre(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaExpreVisitor ) return ((JavaExpreVisitor<? extends T>)visitor).visitVarNameExpre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarNameExpreContext varNameExpre() throws RecognitionException {
		VarNameExpreContext _localctx = new VarNameExpreContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_varNameExpre);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(212);
			match(VarName);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\31\u00d9\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\3\2\3\2\3\2\3\2\3\2\5\28\n\2\3\3\3\3\3\3\3\3\3\3\3\3\5\3@\n\3\3\4\3\4"+
		"\3\4\3\4\3\4\5\4G\n\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5Q\n\5\3\6\3\6"+
		"\3\6\7\6V\n\6\f\6\16\6Y\13\6\3\7\3\7\3\7\3\b\3\b\3\b\3\b\5\bb\n\b\3\t"+
		"\3\t\5\tf\n\t\3\t\3\t\6\tj\n\t\r\t\16\tk\3\n\3\n\3\n\3\n\3\13\3\13\3\13"+
		"\5\13u\n\13\3\13\3\13\3\13\7\13z\n\13\f\13\16\13}\13\13\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\5\f\u0086\n\f\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u008e\n\r\3\16"+
		"\3\16\3\16\3\16\3\16\5\16\u0095\n\16\3\17\3\17\3\17\7\17\u009a\n\17\f"+
		"\17\16\17\u009d\13\17\3\20\3\20\3\20\3\20\3\20\5\20\u00a4\n\20\3\21\3"+
		"\21\3\21\5\21\u00a9\n\21\3\21\3\21\3\21\3\21\5\21\u00af\n\21\7\21\u00b1"+
		"\n\21\f\21\16\21\u00b4\13\21\3\22\3\22\3\22\3\22\3\22\5\22\u00bb\n\22"+
		"\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\5\24\u00c6\n\24\3\25\3\25"+
		"\3\25\3\25\3\26\3\26\3\26\3\26\3\26\5\26\u00d1\n\26\3\27\3\27\3\30\3\30"+
		"\3\31\3\31\3\31\2\2\32\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,"+
		".\60\2\3\3\2\3\4\2\u00e2\2\67\3\2\2\2\4?\3\2\2\2\6F\3\2\2\2\bP\3\2\2\2"+
		"\nR\3\2\2\2\fZ\3\2\2\2\16]\3\2\2\2\20e\3\2\2\2\22m\3\2\2\2\24t\3\2\2\2"+
		"\26~\3\2\2\2\30\u0087\3\2\2\2\32\u0094\3\2\2\2\34\u0096\3\2\2\2\36\u00a3"+
		"\3\2\2\2 \u00a8\3\2\2\2\"\u00ba\3\2\2\2$\u00bc\3\2\2\2&\u00c5\3\2\2\2"+
		"(\u00c7\3\2\2\2*\u00d0\3\2\2\2,\u00d2\3\2\2\2.\u00d4\3\2\2\2\60\u00d6"+
		"\3\2\2\2\628\5\4\3\2\63\64\7\13\2\2\64\65\5\4\3\2\65\66\7\f\2\2\668\3"+
		"\2\2\2\67\62\3\2\2\2\67\63\3\2\2\28\3\3\2\2\29@\5\30\r\2:@\5\b\5\2;@\5"+
		" \21\2<@\5\34\17\2=@\5$\23\2>@\5(\25\2?9\3\2\2\2?:\3\2\2\2?;\3\2\2\2?"+
		"<\3\2\2\2?=\3\2\2\2?>\3\2\2\2@\5\3\2\2\2AG\5\b\5\2BC\7\13\2\2CD\5\b\5"+
		"\2DE\7\f\2\2EG\3\2\2\2FA\3\2\2\2FB\3\2\2\2G\7\3\2\2\2HQ\5\60\31\2IQ\5"+
		"\f\7\2JQ\5\16\b\2KQ\5\20\t\2LQ\7\7\2\2MQ\7\b\2\2NQ\7\t\2\2OQ\5,\27\2P"+
		"H\3\2\2\2PI\3\2\2\2PJ\3\2\2\2PK\3\2\2\2PL\3\2\2\2PM\3\2\2\2PN\3\2\2\2"+
		"PO\3\2\2\2Q\t\3\2\2\2RW\5\2\2\2ST\7\17\2\2TV\5\2\2\2US\3\2\2\2VY\3\2\2"+
		"\2WU\3\2\2\2WX\3\2\2\2X\13\3\2\2\2YW\3\2\2\2Z[\5\60\31\2[\\\5\22\n\2\\"+
		"\r\3\2\2\2]a\5\24\13\2^_\7\20\2\2_b\5\26\f\2`b\5\22\n\2a^\3\2\2\2a`\3"+
		"\2\2\2ab\3\2\2\2b\17\3\2\2\2cf\5.\30\2df\5\60\31\2ec\3\2\2\2ed\3\2\2\2"+
		"fi\3\2\2\2gh\7\20\2\2hj\5\26\f\2ig\3\2\2\2jk\3\2\2\2ki\3\2\2\2kl\3\2\2"+
		"\2l\21\3\2\2\2mn\7\r\2\2no\5\2\2\2op\7\16\2\2p\23\3\2\2\2qr\5.\30\2rs"+
		"\7\20\2\2su\3\2\2\2tq\3\2\2\2tu\3\2\2\2uv\3\2\2\2v{\5\60\31\2wx\7\20\2"+
		"\2xz\5\60\31\2yw\3\2\2\2z}\3\2\2\2{y\3\2\2\2{|\3\2\2\2|\25\3\2\2\2}{\3"+
		"\2\2\2~\u0085\5\60\31\2\177\u0080\7\13\2\2\u0080\u0086\7\f\2\2\u0081\u0082"+
		"\7\13\2\2\u0082\u0083\5\n\6\2\u0083\u0084\7\f\2\2\u0084\u0086\3\2\2\2"+
		"\u0085\177\3\2\2\2\u0085\u0081\3\2\2\2\u0086\27\3\2\2\2\u0087\u008d\5"+
		"\36\20\2\u0088\u0089\7\25\2\2\u0089\u008a\5\6\4\2\u008a\u008b\7\26\2\2"+
		"\u008b\u008c\5\32\16\2\u008c\u008e\3\2\2\2\u008d\u0088\3\2\2\2\u008d\u008e"+
		"\3\2\2\2\u008e\31\3\2\2\2\u008f\u0095\5\30\r\2\u0090\u0091\7\13\2\2\u0091"+
		"\u0092\5\30\r\2\u0092\u0093\7\f\2\2\u0093\u0095\3\2\2\2\u0094\u008f\3"+
		"\2\2\2\u0094\u0090\3\2\2\2\u0095\33\3\2\2\2\u0096\u009b\5\"\22\2\u0097"+
		"\u0098\7\22\2\2\u0098\u009a\5\"\22\2\u0099\u0097\3\2\2\2\u009a\u009d\3"+
		"\2\2\2\u009b\u0099\3\2\2\2\u009b\u009c\3\2\2\2\u009c\35\3\2\2\2\u009d"+
		"\u009b\3\2\2\2\u009e\u00a4\5\34\17\2\u009f\u00a0\7\13\2\2\u00a0\u00a1"+
		"\5\34\17\2\u00a1\u00a2\7\f\2\2\u00a2\u00a4\3\2\2\2\u00a3\u009e\3\2\2\2"+
		"\u00a3\u009f\3\2\2\2\u00a4\37\3\2\2\2\u00a5\u00a9\5\6\4\2\u00a6\u00a9"+
		"\5&\24\2\u00a7\u00a9\5*\26\2\u00a8\u00a5\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a8"+
		"\u00a7\3\2\2\2\u00a9\u00b2\3\2\2\2\u00aa\u00ae\7\21\2\2\u00ab\u00af\5"+
		"\6\4\2\u00ac\u00af\5&\24\2\u00ad\u00af\5*\26\2\u00ae\u00ab\3\2\2\2\u00ae"+
		"\u00ac\3\2\2\2\u00ae\u00ad\3\2\2\2\u00af\u00b1\3\2\2\2\u00b0\u00aa\3\2"+
		"\2\2\u00b1\u00b4\3\2\2\2\u00b2\u00b0\3\2\2\2\u00b2\u00b3\3\2\2\2\u00b3"+
		"!\3\2\2\2\u00b4\u00b2\3\2\2\2\u00b5\u00bb\5 \21\2\u00b6\u00b7\7\13\2\2"+
		"\u00b7\u00b8\5 \21\2\u00b8\u00b9\7\f\2\2\u00b9\u00bb\3\2\2\2\u00ba\u00b5"+
		"\3\2\2\2\u00ba\u00b6\3\2\2\2\u00bb#\3\2\2\2\u00bc\u00bd\5\6\4\2\u00bd"+
		"\u00be\7\24\2\2\u00be\u00bf\5\6\4\2\u00bf%\3\2\2\2\u00c0\u00c6\5$\23\2"+
		"\u00c1\u00c2\7\13\2\2\u00c2\u00c3\5$\23\2\u00c3\u00c4\7\f\2\2\u00c4\u00c6"+
		"\3\2\2\2\u00c5\u00c0\3\2\2\2\u00c5\u00c1\3\2\2\2\u00c6\'\3\2\2\2\u00c7"+
		"\u00c8\5\6\4\2\u00c8\u00c9\7\23\2\2\u00c9\u00ca\5\6\4\2\u00ca)\3\2\2\2"+
		"\u00cb\u00d1\5(\25\2\u00cc\u00cd\7\13\2\2\u00cd\u00ce\5(\25\2\u00ce\u00cf"+
		"\7\f\2\2\u00cf\u00d1\3\2\2\2\u00d0\u00cb\3\2\2\2\u00d0\u00cc\3\2\2\2\u00d1"+
		"+\3\2\2\2\u00d2\u00d3\t\2\2\2\u00d3-\3\2\2\2\u00d4\u00d5\7\5\2\2\u00d5"+
		"/\3\2\2\2\u00d6\u00d7\7\6\2\2\u00d7\61\3\2\2\2\27\67?FPWaekt{\u0085\u008d"+
		"\u0094\u009b\u00a3\u00a8\u00ae\u00b2\u00ba\u00c5\u00d0";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}