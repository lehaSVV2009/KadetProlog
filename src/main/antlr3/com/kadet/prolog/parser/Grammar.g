grammar Grammar;

options {
  language = Java;
}

@lexer::header {
package com.kadet.prolog.parser;
}

@header {
package com.kadet.prolog.parser;
  import com.kadet.prolog.datastructure.*;
  import com.kadet.prolog.solver.*;
}

@members {
//List of avialiable consts
public static ArrayList<String> consts = new ArrayList<String>();
//List of avialiable consts
public static ArrayList<String> vars = new ArrayList<String>();
public static ArrayList<Atom> preds = new ArrayList<Atom>();
public static ArrayList<Atom> goals = new ArrayList<Atom>();
public static ArrayList<Relation> sims = new ArrayList<Relation>();
public static ArrayList<Rule> rules = new ArrayList<Rule>();

public static Base parse(String baseFile, String targetFile) throws Exception {
	GrammarLexer lex = new GrammarLexer(new ANTLRFileStream(baseFile));
	GrammarParser parser = new GrammarParser(new CommonTokenStream(lex));
	try {
		parser.baze();
	} catch (Exception e) {
		System.out.println("Error in parsig base");
	}
	lex = new GrammarLexer(new ANTLRFileStream(targetFile));
	parser = new GrammarParser(new CommonTokenStream(lex));
	try {
		parser.goal();
	} catch (Exception e) {
		System.out.println("Error in parsing target");
	}
	return new Base(consts, vars, preds, goals, sims, rules);
}

public static void main(String[] args) throws Exception {
	GrammarLexer lex = new GrammarLexer(new ANTLRFileStream("baze.txt"));
	GrammarParser parser = new GrammarParser(new CommonTokenStream(lex));
	try {
		parser.baze();
	} catch (Exception e) {
		System.out.println("Compilation failed!");
	}
	lex = new GrammarLexer(new ANTLRFileStream("goal.txt"));
	parser = new GrammarParser(new CommonTokenStream(lex));
	try {
		parser.goal();
	} catch (Exception e) {
		System.out.println("Compilation failed!");
	}
	//Analyzer data = new Analyzer(consts, vars, preds, goals, sims, rules);
}
}

baze
  :
  fact_list rule_list?
  ;

fact_list
  :
  fact (fact)*
  ;

rule_list
  :
  (rule)+
  ;

fact
  :
  pred_fact
  | like_rel_link
  ;

pred_fact returns [Atom p]
  :
  n=pred_name '(' l=const_list ')' '.' 
    {
     Atom pr = new Atom(n, l);
     preds.add(pr);
    }
  ;

like_rel_link returns [Relation rel]
  :
  n=like_rel_name p=like_name_pair '.' 
    {
     $rel = new Relation(n, p);
     sims.add($rel);
    }
  ;

like_name_pair returns [ArrayList<String> list]
  :
  p1=pred_name_pair 
    {
     $list = p1;
    }
  | p2=const_name_pair 
    {
     $list = p2;
    }
  ;

pred_name_pair returns [ArrayList<String> list]
  :
  
    {
     $list = new ArrayList<String>();
    }
  '(' c1=pred_name 
    {
     $list.add(c1);
    }
  ',' c2=pred_name 
    {
     $list.add(c2);
    }
  ')'
  ;

const_name_pair returns [ArrayList<String> list]
  :
  
    {
     $list = new ArrayList<String>();
    }
  '(' c1=const_name 
    {
     $list.add(c1);
    }
  ',' c2=const_name 
    {
     $list.add(c2);
    }
  ')'
  ;

rule returns [Rule rule]
  :
  p=pred_term '<-' l=pred_term_list '.' 
    {
     $rule = new Rule(p, l);
     rules.add(rule);
    }
  ;

const_list returns [ArrayList<String> list]
  :
  
    {
     $list = new ArrayList<String>();
    }
  c1=const_name 
    {
     $list.add(c1);
    }
  (',' c2=const_name 
    {
     $list.add(c2);
    })*
  ;

pred_term_list returns [ArrayList<Atom> list]
  :
  
    {
     $list = new ArrayList<Atom>();
    }
  t1=pred_term 
    {
     $list.add(t1);
    }
  (';' t2=pred_term 
    {
     $list.add(t2);
    })*
  ;

pred_term returns [Atom pred]
  :
  n=pred_name '(' (l=param_list)? ')' 
    {
     Atom pr = new Atom(n, l);
     $pred = pr;
    }
  ;

param_list returns [ArrayList<String> list]
  :
  
    {
     $list = new ArrayList<String>();
    }
  p1=param_name 
    {
     $list.add(p1);
    }
  (',' p2=param_name 
    {
     $list.add(p2);
    })*
  ;

param_name returns [String name]
  :
  (
    v=var_name 
      {
       $name = v;
      }
    | c=const_name 
      {
       $name = c;
      }
  )
  ;

pred_name returns [String name]
  :
  n=up_name 
    {
     $name = n;
    }
  ;

var_name returns [String name]
  :
  n=up_name 
    {
     if (!(vars.contains(n))) {
     	vars.add(n);
     }
     $name = n;
    }
  ;

const_name returns [String name]
  :
  n=low_name 
    {
     $name = n;
     if (!(consts.contains(n))) {
     	consts.add(n);
     }
    }
  ;

like_rel_name returns [String name]
  :
  n=low_name 
    {
     $name = n;
    }
  ;

low_name returns [String s]
  :
  l1=LOW_LETTER 
    {
     String s1 = $l1.getText();
    }
  (l2=LOW_LETTER 
    {
     s1 = s1 + $l2.getText();
    })? 
      {
       $s = s1;
      }
  ;

up_name returns [String s]
  :
  l1=UP_LETTER 
    {
     String s1 = $l1.getText();
    }
  (l2=UP_LETTER 
    {
     s1 = s1 + $l2.getText();
    })? 
      {
       $s = s1;
      }
  ;

//Goal description

goal
  :
  goal_term_list
  ;

goal_term_list
  :
  goal_term (',' goal_term)*
  ;

goal_term returns [Atom pred]
  :
  p=pred_name '(' l=goal_param_list? ')' 
    {
     $pred = new Atom(p, l);
     goals.add(pred);
    }
  ;

goal_param_list returns [ArrayList<String> list]
  :
  
    {
     $list = new ArrayList<String>();
    }
  g1=goal_param_name 
    {
     $list.add(g1);
    }
  (',' g2=goal_param_name 
    {
     $list.add(g2);
    })*
  ;

goal_param_name returns [String name]
  :
  n1='?' 
    {
     $name = "?";
    }
  | n2=param_name 
    {
     $name = n2;
    }
  ;

UP_LETTER
  :
  'A'..'Z'
  ;

LOW_LETTER
  :
  'a'..'z'
  ;

SPACE
  :
  (
    ' '
    | '\n'
    | '\t'
    | '\r'
  )
  
    {
     $channel = HIDDEN;
    }
  ;

COMMENT
  :
  '//'
  ~(
    '\n'
    | '\r'
   )*
  '\r'? '\n' 
    {
     $channel = HIDDEN;
    }
  | '/*' (options {greedy=false;}: .)* '*/' 
    {
     $channel = HIDDEN;
    }
  ;
