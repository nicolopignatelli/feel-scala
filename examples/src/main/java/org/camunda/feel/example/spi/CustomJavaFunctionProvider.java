/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.feel.example.spi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.camunda.feel.interpreter.ValNumber;
import org.camunda.feel.spi.JavaFunction;
import org.camunda.feel.spi.JavaFunctionProvider;

import scala.math.BigDecimal;

public class CustomJavaFunctionProvider implements JavaFunctionProvider
{
    private static final Map<String, JavaFunction> functions = new HashMap<>();

    static {

        functions.put("bar", new JavaFunction(Arrays.asList("x"), args -> {
            final ValNumber arg = (ValNumber) args.get(0);

            int x = arg.value().intValue();

            return new ValNumber(BigDecimal.valueOf(x - 1));
        }));

    }

    @Override
    public Optional<JavaFunction> resolveFunction(String functionName, int argCount)
    {
        final JavaFunction function = functions.get(functionName);

        if (function != null && function.getParams().size() == argCount)
        {
            return Optional.of(function);
        }
        else
        {
            return Optional.empty();
        }
    }

}
