package com.onnasoft.NSPMobil.views.canvas;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.ArrayList;

import kr.neolab.sdk.graphic.Renderer;
import kr.neolab.sdk.ink.structure.Dot;
import kr.neolab.sdk.ink.structure.DotType;
import kr.neolab.sdk.ink.structure.Stroke;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.onnasoft.NSPMobil.store.Store;
import com.onnasoft.NSPMobil.store.Subscribe;

public class PaperView extends SurfaceView implements SurfaceHolder.Callback
{
    private SampleThread mSampleThread;

	// paper background
	private Bitmap background = null;

	// draw the strokes
	private ArrayList<Stroke> strokes = new ArrayList<Stroke>();

	private Stroke stroke = null;

	private int sectionId = 0, ownerId = 0, noteId = 0, pageId = 0;

	private float scale = 11, offsetX = 0, offsetY = 0;

	public static Subscribe subscribe = new Subscribe(){
	    private PaperView mPaperView;
        @Override
	    public void setContext(Object mPaperView) {
	        this.mPaperView = (PaperView) mPaperView;
        }
        @Override
        public void reload() {
	        if(this.mPaperView == null) return;
            this.mPaperView.reload();
        }
    };

	private static PaperView paperView;

	public static PaperView getInstance() {
		return paperView;
	}

	public PaperView( Context context )
	{
		super( context );

		getHolder().addCallback( this );
		mSampleThread = new SampleThread( this.getHolder(), this );

		subscribe.setContext(this);

        Store.subscribe(subscribe);

		paperView = this;
    }


	public Bitmap getBackgroundImage() {
		return background;
	}

	public void setPageSize(float width, float height )
	{
		if ( getWidth() <= 0 || getHeight() <= 0 || width <= 0 || height <= 0 )
		{
			return;
		}

		float width_ratio = getWidth() / width;
		float height_ratio = getHeight() / height;

		scale = Math.min( width_ratio, height_ratio );

		int docWidth = (int) (width * scale);
		int docHeight = (int) (height * scale);

		int mw = getWidth() - docWidth;
		int mh = getHeight() - docHeight;

		offsetX = mw / 2;
		offsetY = mh / 2;

        Bitmap source = (Bitmap) Store.getState().get("template");
        if (source != null) {
            background = Bitmap.createBitmap(background, 0, 0, docWidth, docHeight);
        } else {
            background = Bitmap.createBitmap( docWidth, docHeight, Bitmap.Config.ARGB_8888 );
            background.eraseColor( Color.parseColor( "#F9F9F9" ) );
        }
	}

	public void reload() {
		try {
			Bitmap templ = (Bitmap) Store.getState().get("template");
			if (templ == null) return;
			background = templ;
			Log.d("paperview", "aplica el nuevo template");
		} catch (Exception e) {

		}
    }

	@Override
	public void draw( Canvas canvas )
	{
		canvas.drawColor( Color.LTGRAY );

		if ( background != null )
		{
            Rect dest = new Rect(0, 0, getWidth(), getHeight());
            Paint paint = new Paint();
            paint.setFilterBitmap(true);
            canvas.drawBitmap(background, null, dest, paint);
			//canvas.drawBitmap( background, offsetX, offsetY, null);
		}

		if ( strokes != null && strokes.size() > 0 )
		{
			Renderer.draw(canvas, strokes.toArray(new Stroke[0]), scale, offsetX, offsetY);
		}
	}

	@Override
	public void surfaceChanged( SurfaceHolder arg0, int arg1, int arg2, int arg3 )
	{
	}

	@Override
	public void surfaceCreated( SurfaceHolder arg0 )
	{
		mSampleThread = new SampleThread( getHolder(), this );
		mSampleThread.setRunning( true );
		mSampleThread.start();
	}

	@Override
	public void surfaceDestroyed( SurfaceHolder arg0 )
	{
		mSampleThread.setRunning( false );

		boolean retry = true;

		while ( retry )
		{
			try
			{
				mSampleThread.join();
				retry = false;
			}
			catch ( InterruptedException e )
			{
				e.getStackTrace();
			}
		}
	}

	public void addDot( int sectionId, int ownerId, int noteId, int pageId, int x, int y, int fx, int fy, int force, long timestamp, int type, int color )
	{
		if ( this.sectionId != sectionId || this.ownerId != ownerId || this.noteId != noteId || this.pageId != pageId )
		{
			strokes = new ArrayList<Stroke>();

			this.sectionId = sectionId;
			this.ownerId = ownerId;
			this.noteId = noteId;
			this.pageId = pageId;
		}

		if ( DotType.isPenActionDown( type ) || stroke == null || stroke.isReadOnly() )
		{
			stroke = new Stroke( sectionId, ownerId, noteId, pageId, color );
			strokes.add( stroke );
		}

		stroke.add( new Dot( x, y, fx, fy, force, type, timestamp ) );
	}

	public void addStrokes( Stroke[] strs )
	{
		for ( Stroke stroke : strs )
		{
			strokes.add( stroke );
		}
	}

	public class SampleThread extends Thread
	{
		private SurfaceHolder surfaceholder;
		private PaperView mPaperiView;
		private boolean running = false;

		public SampleThread( SurfaceHolder surfaceholder, PaperView mView )
		{
			this.surfaceholder = surfaceholder;
			this.mPaperiView = mView;
		}

		public void setRunning( boolean run )
		{
			running = run;
		}

		@Override
		public void run()
		{
			setName( "SampleThread" );

			Canvas mCanvas;

			while ( running )
			{
				mCanvas = null;

				try
				{
					mCanvas = surfaceholder.lockCanvas(); // lock canvas

					synchronized ( surfaceholder )
					{
						if ( mCanvas != null )
						{
							mPaperiView.draw( mCanvas );
						}
					}
				}
				finally
				{
					if ( mCanvas != null )
					{
						surfaceholder.unlockCanvasAndPost( mCanvas ); // unlock
																		// canvas
					}
				}
			}
		}
	}
}
