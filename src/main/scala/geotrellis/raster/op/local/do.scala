package geotrellis.raster.op.local

import geotrellis._
import RasterUtil._

/**
 * Perform a function on every cell in a raster.
 *
 * @example
 * <pre>
 * val r = LoadFile(f)
 * val result = DoCell(R, {x:Int => x + 3} ) // add 3 to every cell in the raster  
 * </pre>
 */
case class DoCell(r:Op[Raster], f:Int => Int) extends Op1(r)({
  (r) => Result(r.dualMap(f)({ z:Double => i2d(f(d2i(z))) }))
})

object DoCell {
  def apply(r:Op[Raster], f:Double => Double) = DoDoubleCell(r,f)
  def apply(r1:Op[Raster], r2:Op[Raster], f:(Int,Int) => Int) = BinaryDoCell(r1,r2,f)
  def apply(r1:Op[Raster], r2:Op[Raster], f:(Double,Double) => Double) = BinaryDoDoubleCell(r1,r2,f)
}

/**
 * Perform a Double function on every cell in a raster.
 *
 * @example
 * <pre>
 * val r = LoadFile(f)
 * val r2 = DoCell(R, x => x + 3 ) // add 3 to every cell in the raster  
 * </pre>
 */
case class DoDoubleCell(r:Op[Raster], f:Double => Double) extends Op1(r)({
  (r) => Result(r.dualMap({ z:Int => d2i(f(i2d(z))) })(f))
})

/**
 * Perform a function on every cell in a raster with the values from another raster.
 *
 * @example
 * <pre>
 * val r1 = LoadFile(a)
 * val r2 = LoadFile(b)
 * 
 * // Generate a raster by adding the values of each cell in A and B 
 * val result = BinaryDoCell(r1, r2, {(a:Int, b:Int) => a + b} )
 * </pre>
 */
case class BinaryDoCell(r1:Op[Raster], r2:Op[Raster], f:(Int, Int) => Int) extends BinaryLocal {
  def handle(z1:Int, z2:Int) = f(z1, z2)
  def handleDouble(z1:Double, z2:Double) = i2d(f(d2i(z1), d2i(z2)))
}

/**
 * Perform a Double function on every cell in a raster with the values from another raster.
 *
 * @example
 * <pre>
 * val r1 = LoadFile(a)
 * val r2 = LoadFile(b)
 * 
 * // Generate a raster by adding the values of each cell in A and B 
 * val result = BinaryDoCell(r1, r2, {(a:Double, b:Double) => a + b} )
 * </pre>
 */
case class BinaryDoDoubleCell(r1:Op[Raster], r2:Op[Raster], f:(Double, Double) => Double) extends BinaryLocal {
  def handle(z1:Int, z2:Int) = d2i(f(i2d(z1), i2d(z2)))
  def handleDouble(z1:Double, z2:Double) = f(z1, z2)
}
