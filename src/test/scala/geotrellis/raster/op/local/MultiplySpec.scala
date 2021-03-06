package geotrellis.raster.op.local

import geotrellis._

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

import geotrellis.testutil._

@org.junit.runner.RunWith(classOf[org.scalatest.junit.JUnitRunner])
class MultiplySpec extends FunSpec 
                      with ShouldMatchers 
                      with TestServer 
                      with RasterBuilders {
  describe("Multiply") {
    it("multiplys two integers") {
      run(Multiply(3,2)) should be (6)
    }

    it("multiplys two doubles") {
      run(Multiply(.2,.3)) should be (.06)
    }
    
    it("multiplys a constant value to each cell of an int valued raster") {
      val r = positiveIntegerRaster
      val result = run(Multiply(r,5))
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          result.get(col,row) should be (r.get(col,row) * 5)
        }
      }
    }

    it("multiplys a constant value to each cell of an double valued raster") {
      val r = probabilityRaster
      val result = run(Multiply(r,3))
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          result.getDouble(col,row) should be (r.getDouble(col,row) * 3.0)
        }
      }
    }

    it("multiplys a double constant value to each cell of an int valued raster") {
      val r = positiveIntegerRaster
      val result = run(Multiply(r,5.1))
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          result.get(col,row) should be ((r.get(col,row) * 5.1).toInt)
        }
      }
    }

    it("multiplys a double constant value to each cell of an double valued raster") {
      val r = probabilityRaster
      val result = run(Multiply(r,.3))
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          result.getDouble(col,row) should be (r.getDouble(col,row) * 0.3)
        }
      }
    }

    it("multiplys an integer raster to itself") {
      val r = positiveIntegerRaster
      val result = run(Multiply(r,r))
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          result.get(col,row) should be (math.pow(r.get(col,row),2.0))
        }
      }
    }
    
    it("multiplys a double raster to itself") {
      val r = probabilityRaster
      val result = run(Multiply(r,r))
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          result.getDouble(col,row) should be (math.pow(r.getDouble(col,row), 2.0))
        }
      }
    }
  }
}
