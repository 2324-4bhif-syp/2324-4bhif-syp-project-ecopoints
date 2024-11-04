import { RouterModule, Routes } from "@angular/router";
import { AppComponent } from "./app.component";
import { NgModule } from "@angular/core";
import { GraphOverviewComponent } from "./graph-overview/graph-overview.component";
import { BrowserModule } from "@angular/platform-browser";
import { HttpClientModule } from "@angular/common/http";
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; 
import { GraphComponent } from "./graph/graph.component";
import { ModifyGraphsComponent } from "./modify-graphs/modify-graphs.component";

const appRoutes: Routes = [
    { path: '', component: GraphOverviewComponent },
    { path: 'modify-graphs', component: ModifyGraphsComponent }
];

@NgModule({
    declarations: [
        AppComponent,
        GraphOverviewComponent,
        GraphComponent,
        ModifyGraphsComponent
    ],
    imports: [

        BrowserModule,
        RouterModule.forRoot(appRoutes),
        HttpClientModule,
        FormsModule,
        BrowserAnimationsModule,
        
    ],
    bootstrap: [AppComponent]

})

export class AppModule { }
