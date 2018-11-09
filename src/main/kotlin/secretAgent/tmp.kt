package secretAgent

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D

operator fun Vector2D.plus(b: Vector2D) = Vector2D(x + b.x, y + b.y)
operator fun Vector2D.times(b: Double) = Vector2D(x * b, y * b)