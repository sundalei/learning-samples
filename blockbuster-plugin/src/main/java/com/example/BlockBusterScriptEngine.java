package com.example;

import java.util.Map;
import java.util.Set;
import org.elasticsearch.logging.LogManager;
import org.elasticsearch.logging.Logger;
import org.elasticsearch.script.ScriptContext;
import org.elasticsearch.script.ScriptEngine;
import org.elasticsearch.script.UpdateScript;

public class BlockBusterScriptEngine implements ScriptEngine {

  private static final Logger logger = LogManager.getLogger(BlockBusterScriptEngine.class);

  @Override
  public String getType() {
    return "java";
  }

  @Override
  public <FactoryType> FactoryType compile(
      String name,
      String code,
      ScriptContext<FactoryType> context,
      Map<String, String> compilerParams) {
    logger.info("Compiling script. Context: [{}] Code: [{}]", context.name, code);

    if ("CustomBlockBusterCheck".equals(code)) {
      if (context.name.equals(UpdateScript.CONTEXT.name)) {
        UpdateScript.Factory factory =
            (params, ctx) -> {
              return new UpdateScript(params, ctx) {

                @Override
                public void execute() {
                  Map<String, Object> source = ctx.getSource();
                  logger.info("params {}", params);
                  CustomBlockBusterCheck myLogic = new CustomBlockBusterCheck();
                  myLogic.execute(source, params);
                }
              };
            };

        return context.factoryClazz.cast(factory);
      }
    }
    throw new IllegalArgumentException(
        "Unsupported script [" + code + "] for context [" + context.name + "]");
  }

  @Override
  public Set<ScriptContext<?>> getSupportedContexts() {
    return Set.of(UpdateScript.CONTEXT);
  }
}
