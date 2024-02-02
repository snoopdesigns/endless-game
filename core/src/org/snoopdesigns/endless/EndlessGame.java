package org.snoopdesigns.endless;

import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.utils.ScreenUtils;
import org.snoopdesigns.endless.context.Context;
import org.snoopdesigns.endless.context.OnInit;
import org.snoopdesigns.endless.physics.Box2DDebugRenderer;
import org.snoopdesigns.endless.physics.Box2DRenderer;
import org.snoopdesigns.endless.physics.camera.CameraRenderer;
import org.snoopdesigns.endless.renderer.CursorRenderer;
import org.snoopdesigns.endless.renderer.DebugRenderer;
import org.snoopdesigns.endless.renderer.Renderer;
import org.snoopdesigns.endless.utils.ExitControllable;
import org.snoopdesigns.endless.world.ObjectsRenderer;
import org.snoopdesigns.endless.world.bg.BackgroundRenderer;

public class EndlessGame extends ApplicationAdapter {

	private final List<Renderer> renderers = List.of(
			new BackgroundRenderer(),
			new Box2DRenderer(),
			new CameraRenderer(),
			new Box2DDebugRenderer(),
			new ObjectsRenderer(),
			new CursorRenderer(),
			new DebugRenderer()
	);

	private final List<OnInit> intializers = List.of(
			new Context()
	);
	
	@Override
	public void create () {
		intializers.forEach(OnInit::create);

		final InputMultiplexer inputMultiplexer = new InputMultiplexer();
		renderers.forEach(inputMultiplexer::addProcessor);
		inputMultiplexer.addProcessor(Context.getInstance().getPlayerShip());
		inputMultiplexer.addProcessor(new ExitControllable());
		Gdx.input.setInputProcessor(inputMultiplexer);

		renderers.forEach(Renderer::create);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		renderers.forEach(Renderer::render);
	}

	@Override
	public void resize(int width, int height) {
		renderers.forEach(renderer ->
				renderer.resize(width, height));
	}
	
	@Override
	public void dispose() {
		renderers.forEach(Renderer::dispose);
	}
}
