package org.apache.ibatis.builder.xml.dynamic;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlException;
import org.apache.ibatis.parsing.GenericTokenParser;

public class TextSqlNode implements SqlNode {
  private String text;

  public TextSqlNode(String text) {
    this.text = text;
  }

  public boolean apply(DynamicContext context) {
    GenericTokenParser parser = new GenericTokenParser("${", "}", new BindingTokenParser(context));
    context.appendSql(parser.parse(text));
    return true;
  }

  private static class BindingTokenParser implements GenericTokenParser.TokenHandler {

    private DynamicContext context;

    public BindingTokenParser(DynamicContext context) {
      this.context = context;
    }

    public String handleToken(String content) {
      try {
        Object value = Ognl.getValue(content, context.getBindings());
        return String.valueOf(value);
      } catch (OgnlException e) {
        throw new BuilderException("Error evaluating expression '" + content + "'. Cause: " + e, e);
      }
    }
  }


}

