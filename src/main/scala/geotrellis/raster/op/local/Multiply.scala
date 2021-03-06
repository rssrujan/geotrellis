package geotrellis.raster.op.local

import geotrellis._

/**
 * Multiplies values of Rasters or constants.
 */
object Multiply {
  /** Multiply two integer values. */
  def apply(x:Op[Int], y:Op[Int]) = logic.Do2(x, y)((x, y) => x * y)

  /** Multiply two integer values. */
  def apply(x:Op[Double], y:Op[Double])(implicit d: DummyImplicit) = logic.Do2(x, y)((x, y) => x * y)

  /** Multiply each cell by an Int constant. See [[MultiplyConstant]] */
  def apply(r:Op[Raster], c:Op[Int]) = MultiplyConstant(r, c)

  /** Multiply each cell by an Int constant. See [[MultiplyConstant]] */
  def apply(c:Op[Int], r:Op[Raster])(implicit d: DummyImplicit) = MultiplyConstant(r, c)

  /** Multiply each cell by an Double constant. See [[MultiplyDoubleConstant]] */
  def apply(r:Op[Raster], c:Op[Double]) = MultiplyDoubleConstant(r, c)

  /** Multiply each cell by an Double constant. See [[MultiplyDoubleConstant]] */
  def apply(c:Op[Double], r:Op[Raster])(implicit d: DummyImplicit) = MultiplyDoubleConstant(r, c)

  /** Multiply values of cells of each raster. See [[MultiplyRasters]] */
  def apply(rs:Op[Raster]*) = MultiplyRasters(rs:_*)

  /** Multiply values of cells of each raster. See [[MultiplyArray]] */
  def apply(rs:Op[Array[Raster]]) = MultiplyArray(rs)
}

/**
 * Multiply each cell by a constant.
 */
case class MultiplyConstant(r:Op[Raster], c:Op[Int]) extends Op2(r, c)({
  (r, c) => Result(r.dualMapIfSet(_ * c)(_ * c))
})

/**
 * Multiply each cell by a constant (double).
 */
case class MultiplyDoubleConstant(r:Op[Raster], c:Op[Double]) extends Op2(r, c)({
  (r, c) => Result(r.dualMapIfSet({ i:Int => (i * c).toInt})(_ * c))
})

/**
 * Multiply each cell of each raster.
 */
case class MultiplyRasters(rs:Op[Raster]*) extends MultiLocal {
  final def ops = rs.toArray
  final def handle(a:Int, b:Int) = if (a == NODATA) NODATA else if (b == NODATA) NODATA else a * b
  final def handleDouble(a:Double, b:Double) = a * b
}

/**
 * Multiply each cell of each raster in array.
 */
case class MultiplyArray(op:Op[Array[Raster]]) extends MultiLocalArray {
  final def handle(a:Int, b:Int) = if (a == NODATA) NODATA else if (b == NODATA) NODATA else a * b
  final def handleDouble(a:Double, b:Double) = a * b
}
