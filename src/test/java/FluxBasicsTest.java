import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.time.LocalDateTime;

class FluxBasicsTest {

  @Test
  @DisplayName("Flux of three integer then expect next three values, or count of three")
  void fluxTest() {
    Flux<Integer> stringFlux = Flux.range(1, 3);

    StepVerifier.create(stringFlux)
      .expectNext(1)
      .expectNext(2)
      .expectNext(3)
      .verifyComplete();

    StepVerifier.create(stringFlux)
      .expectNextCount(3)
      .verifyComplete();
  }

  @Test
  @DisplayName("Flux of three integer then expect next three values, or count of three")
  void fluxTest1() {
    Flux<Long> stringFlux = Flux.interval(Duration.ofMillis(2)).take(2);

    StepVerifier.create(stringFlux)
      .expectNext(0L)
      .expectNext(1L)
      .verifyComplete();

  }

  @Test
  @DisplayName("Filter test")
  void fluxTest2() {
    Flux<String> stringFlux = Flux.just("a", "b", "c")
      .filter(s -> !s.equalsIgnoreCase("c"));

    StepVerifier.create(stringFlux)
      .expectNext("a")
      .expectNext("b")
      .verifyComplete();
  }

  @Test
  @DisplayName("Map test")
  void fluxTest3() {
    Flux<Integer> stringFlux = Flux.just("1", "2", "3")
      .map(Integer::parseInt);

    StepVerifier.create(stringFlux)
      .expectNext(1)
      .expectNext(2)
      .expectNext(3)
      .verifyComplete();
  }

  @Test
  @DisplayName("flatMap test")
  void fluxTest4() {
    Flux<Integer> stringFlux = Flux.just("1", "2", "3")
      .flatMap(s -> Mono.just(Integer.parseInt(s)));

    StepVerifier.create(stringFlux)
      .expectNext(1)
      .expectNext(2)
      .expectNext(3)
      .verifyComplete();
  }

  @Test
  @DisplayName("Zip test, values zipped vertically")
  void fluxTest6() {
    Flux<String> stringFlux1 = Flux.just("1", "2");
    Flux<String> stringFlux2 = Flux.just("3", "4");

    Flux<Tuple2<String, String>> zippedFlux = Flux.zip(stringFlux1, stringFlux2);

    StepVerifier.create(zippedFlux)
      .expectNext(Tuples.of("1", "3"))
      .expectNext(Tuples.of("2", "4"))
      .verifyComplete();
  }

  @Test
  @DisplayName("Concatenation test, when fist flux is terminated then second is concatenated")
  void fluxTest5() {
    Flux<Tuple2<String, Long>> stringFlux1 = Flux.just("1", "2").zipWith(Flux.interval(Duration.ofMillis(5))).take(10);
    Flux<Tuple2<String, Long>> stringFlux2 = Flux.just("3", "4").zipWith(Flux.interval(Duration.ofMillis(1))).take(2);

    Flux<Tuple2<String, Long>> concatFlux = Flux.concat(stringFlux1, stringFlux2)
      .doOnNext(aLong -> System.out.println(aLong + ":" + LocalDateTime.now()));

    StepVerifier.create(concatFlux)
      .expectNext(Tuples.of("1", 0L))
      .expectNext(Tuples.of("2", 1L))
      .expectNext(Tuples.of("3", 0L))
      .expectNext(Tuples.of("4", 1L))
      .verifyComplete();
  }

  @Test
  @DisplayName("Merge test")
  void fluxTest7() {
    Flux<Tuple2<String, Long>> stringFlux1 = Flux.just("1", "2")
      .zipWith(Flux.interval(Duration.ofMillis(200)))
      .take(2);

    Flux<Tuple2<String, Long>> stringFlux2 = Flux.just("3", "4")
      .zipWith(Flux.interval(Duration.ofMillis(100)))
      .take(2);

    Flux<Tuple2<String, Long>> concatFlux = Flux.merge(stringFlux1, stringFlux2)
      .doOnNext(aLong -> System.out.println(aLong + ":" + LocalDateTime.now()));

    StepVerifier.create(concatFlux)
      .expectNext(Tuples.of("3", 0L))
      .expectNext(Tuples.of("1", 0L))
      .expectNext(Tuples.of("4", 1L))
      .expectNext(Tuples.of("2", 1L))
      .verifyComplete();
  }
}
