import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { GalleryComponent } from './gallery/gallery/gallery.component';
import { ImageComponent } from './image/image/image.component';
import { ModalComponent } from './image/modal/modal.component';

@NgModule({
  declarations: [
    AppComponent,
    GalleryComponent,
    ImageComponent,
    ModalComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
