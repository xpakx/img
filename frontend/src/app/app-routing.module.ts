import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { GalleryComponent } from './gallery/gallery/gallery.component';
import { ImagePageComponent } from './image/image-page/image-page.component';

const routes: Routes = [
  { path: 'profile/:name', component: GalleryComponent },
  { path: 'profile/:name/image/:id', component: ImagePageComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
