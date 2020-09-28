/*
 * This file is part of the mesoscopic traffic simulation PFaRA of Clauthal University of
 * Technology-Mobile and Enterprise Computing aswell as SocialCars Research Training Group.
 *  Copyright (c) 2017-2021 Sinziana-Maria Sebe (sms14@tu-clausthal.de)
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 *  General Public License as  published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *  PURPOSE.  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program. If not, see http://www.gnu.org/licenses/
 */

package org.socialcars.sinziana.pfara.visualisation;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;


/**
 * route painting class
 * paints a route taken in red
 */
public class CRoutePainter implements Painter<JXMapViewer>
{
    private Color m_color = Color.RED;
    private boolean m_antialias = true;

    private List<GeoPosition> m_track;

    /**
     * ctor
     * @param p_track the track
     */
    public CRoutePainter( final List<GeoPosition> p_track )
    {
        m_track = new ArrayList<>( p_track );
    }

    /**
     * the paint function
     * @param p_gr graphics
     * @param p_map the map
     * @param p_width width of the window
     * @param p_height height of the window
     */
    @Override
    public void paint( final Graphics2D p_gr, final JXMapViewer p_map, final int p_width, final int p_height )
    {
        final Graphics2D l_gr = (Graphics2D) p_gr.create();

        // convert from viewport to world bitmap
        final Rectangle l_rect = p_map.getViewportBounds();
        l_gr.translate( -l_rect.x, -l_rect.y );

        if ( m_antialias )
            l_gr.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

        // do the drawing
        l_gr.setColor( Color.BLACK );
        l_gr.setStroke( new BasicStroke( 2 ) );

        drawRoute( l_gr, p_map );

        // do the drawing again
        l_gr.setColor( m_color );
        l_gr.setStroke( new BasicStroke( 2 ) );


        drawRoute( l_gr, p_map );

        l_gr.dispose();
    }

    /**
     * @param p_gr the graphics object
     * @param p_map the map
     */
    private void drawRoute( final Graphics2D p_gr, final JXMapViewer p_map )
    {
        int l_lastx = 0;
        int l_lasty = 0;

        boolean first = true;

        for ( final GeoPosition l_gp : m_track )
        {
            // convert geo-coordinate to world bitmap pixel
            final Point2D l_pt = p_map.getTileFactory().geoToPixel( l_gp, p_map.getZoom() );

            if ( first )
            {
                first = false;
            }
            else
            {
                p_gr.drawLine( l_lastx, l_lasty, (int) l_pt.getX(), (int) l_pt.getY() );
            }

            l_lastx = (int) l_pt.getX();
            l_lasty = (int) l_pt.getY();
        }
    }
}
