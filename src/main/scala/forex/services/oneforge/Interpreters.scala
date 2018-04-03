package forex.services.oneforge

import forex.domain._
import monix.eval.Task
import org.atnos.eff._
import org.atnos.eff.addon.monix.task._

object Interpreters {

  def live[R](cache: RateCache)(implicit m1: _task[R]): Algebra[Eff[R, ?]] = new Live[R](cache)

}

private[oneforge] final class Live[R](cache: RateCache)(implicit m1: _task[R]) extends Algebra[Eff[R, ?]] {

  override def get(pair: Rate.Pair): Eff[R, Error Either Rate] =
    for {
      result ← fromTask(Task(cache.get(pair)))
    } yield result

}
