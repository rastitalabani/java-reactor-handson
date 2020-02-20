import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Arrays;
import java.util.List;

class MonoBasicsTest {

  @Test
  @DisplayName("Empty Mono")
  void monoTest() {
    Mono<String> stringMono = Mono.empty();

    StepVerifier.create(stringMono)
      .verifyComplete();
  }

  @Test
  @DisplayName("Mono of a String")
  void monoTest1() {
    Mono<String> stringMono = Mono.just("str1");

    StepVerifier.create(stringMono)
      .expectNext("str1")
      .verifyComplete();
  }

  @Test
  @DisplayName("Mono of zipped mono's")
  void monoTest2() {
    Mono<String> stringMono1 = Mono.just("a");
    Mono<String> stringMono2 = Mono.just("b");

    Mono<Tuple2<String, String>> zippedMono = Mono.zip(stringMono1, stringMono2);

    StepVerifier.create(zippedMono)
      .expectNext( Tuples.of("a","b"))
      .verifyComplete();
  }

  @Test
  @DisplayName("Mono of zipped mono's")
  void monoTest3() {
    Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("a","b","c"));

    Mono<List<String>> listMono = stringFlux.collectList();

    StepVerifier.create(listMono)
      .expectNext( Arrays.asList("a","b","c"))
      .verifyComplete();
  }
}
