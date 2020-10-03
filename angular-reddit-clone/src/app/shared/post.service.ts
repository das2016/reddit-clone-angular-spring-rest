import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PostModel } from './post.model';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  url:String = "http://vps-f9968a88.vps.ovh.net:8080";

  constructor(private http: HttpClient) { }

  getAllPosts(): Observable<Array<PostModel>> {
    return this.http.get<Array<PostModel>>(this.url+'/api/posts/');
  }

}
