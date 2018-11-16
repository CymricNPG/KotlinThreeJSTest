import game.Tile
import info.laht.threekt.THREE
import info.laht.threekt.cameras.PerspectiveCamera
import info.laht.threekt.core.BufferGeometry
import info.laht.threekt.external.controls.OrbitControls
import info.laht.threekt.external.libs.Stats
import info.laht.threekt.geometries.BoxBufferGeometry
import info.laht.threekt.geometries.PlaneGeometry
import info.laht.threekt.lights.AmbientLight
import info.laht.threekt.materials.Material
import info.laht.threekt.materials.MeshBasicMaterial
import info.laht.threekt.materials.MeshPhongMaterial
import info.laht.threekt.math.ColorConstants
import info.laht.threekt.objects.Mesh
import info.laht.threekt.renderers.WebGLRenderer
import info.laht.threekt.renderers.WebGLRendererParams
import info.laht.threekt.scenes.Scene
import kotlin.browser.document
import kotlin.browser.window
import kotlin.math.PI
import kotlin.math.min


class Main {

    private val renderer: WebGLRenderer
    private val scene: Scene = Scene()
    private val camera: PerspectiveCamera
    private val controls: OrbitControls
    private val cube: Mesh
    private val stats: Stats = Stats()
    private val planes: MutableList<Tile> = mutableListOf()
    private var lastTime = 0.0
    private var deltaTime = 0.0
    private var tileLength = 1.5
    private var lastPos = 0.0
    private val materialMap = mutableMapOf<Int, Material>()
    private val baseSpeed = 0.01

    private var field: FieldView

    init {

        scene.add(AmbientLight())

        camera = PerspectiveCamera(75, window.innerWidth.toDouble() / window.innerHeight, 0.1, 1000)
        camera.position.setZ(5)

        renderer = WebGLRenderer(WebGLRendererParams(
                antialias = true
        )).apply {
            setClearColor(ColorConstants.skyblue, 1)
            setSize(window.innerWidth, window.innerHeight)
        }

        document.getElementById("container")?.apply {
            appendChild(renderer.domElement)
            appendChild(stats.dom)
        }

        controls = OrbitControls(camera, renderer.domElement)

        cube = Mesh(BoxBufferGeometry(0.1, 0.1, 0.1),
                MeshPhongMaterial().apply {
                    this.color.set(ColorConstants.darkgreen)
                }).also(scene::add)

        Mesh(cube.geometry as BufferGeometry,
                MeshBasicMaterial().apply {
                    this.wireframe = true
                    this.color.set(ColorConstants.black)
                }).also(cube::add)


        val fieldXsize = 10
        val fieldYSize = 50
        val endlessField = EndlessField(fieldXsize)
        field = FieldView(fieldXsize, fieldYSize, endlessField)

        val xsize = 5.0
        val zsize = 5.0
        val width = 0.5
        field.makeView(0)
        for (x in 1..fieldXsize) {
            for (z in 1..fieldYSize) {
                val tileMaterial = getTileMaterial(field.get(x, z), x, z)

                val pgeometry = PlaneGeometry(width, tileLength, 1)

                val plane = Mesh(pgeometry, tileMaterial)
                val posx = -xsize / 2 + width * x
                val posz = zsize / 2 - tileLength * z
                plane.rotateX(PI / 2)
                plane.position.set(posx, -0.5, posz)
                planes.add(Tile(x, z, posz, plane))
                scene.add(plane)


            }
        }
        window.addEventListener("resize", {
            camera.aspect = window.innerWidth.toDouble() / window.innerHeight
            camera.updateProjectionMatrix()

            renderer.setSize(window.innerWidth, window.innerHeight)
        }, false)

    }

    private fun getTileMaterial(tile: TileType, x: Int, z: Int): Material {
        val col = if (x.rem(2) == z.rem(2)) tile.light else tile.dark
        return materialMap.getOrPut(col, {
            MeshBasicMaterial().apply {
                this.color.set(col)
                this.side = THREE.DoubleSide
            }
        })
    }

    fun updateGame() {
        val now = window.performance.now()
        deltaTime = min(now - lastTime, 1000.0)

        val movement = baseSpeed * deltaTime / 1000.0

        lastPos += movement
        val offset = lastPos.rem(tileLength)
        val absPos = lastPos.div(tileLength).toInt()
        field.makeView(absPos)

        cube.rotation.x += 0.01
        cube.rotation.y += 0.01
        for (plane in planes) {
            plane.mesh.position.z = plane.zPos + offset
            plane.mesh.material = getTileMaterial(field.get(x = plane.x, y = plane.y), plane.x,
                    plane.y + absPos)
        }
    }

    fun animate() {
        window.requestAnimationFrame {
            animate()
        }
        updateGame()
        renderer.render(scene, camera)
        stats.update()
    }
}