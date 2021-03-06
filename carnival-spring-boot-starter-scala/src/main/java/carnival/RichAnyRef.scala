/*
 *  ____    _    ____  _   _ _____     ___    _
 * / ___|  / \  |  _ \| \ | |_ _\ \   / / \  | |
 * | |    / _ \ | |_) |  \| || | \ \ / / _ \ | |
 * | |___/ ___ \|  _ <| |\  || |  \ V / ___ \| |___
 * \____/_/   \_\_| \_\_| \_|___|  \_/_/   \_\_____|
 *
 * https://github.com/yingzhuo/carnival
 */
package carnival

/**
  * @author 应卓
  */
private[carnival] class RichAnyRef[T <: AnyRef](o: T) {

  require(o != null)

  // -----------------------------------------------------------------------------------------------------------------

  def some: Option[T] = Some(o)

  def none: Option[T] = None

}
