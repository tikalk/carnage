import React, { Component } from 'react';
import Login from './login/login.component';
import Question from './question/question.component';
import axios from 'axios';
import './App.css';

class App extends Component {

  index = 1;
  loggedIn = false;
  static URL = 'https://807npuj887.execute-api.us-west-1.amazonaws.com/dev/question/'

  // question = {
  //   question: "what is your name?",
  //   answers: [
  //     'Roy',
  //     'Itay',
  //     'Keren',
  //     'Pinhas'
  //   ]
  // }

  render() {
    let self = this;
    return (
      <div className="App">
      {
        !this.loggedIn && <Login onLogin={() => {self.logInUser()}}></Login>
      }      
      {
        this.loggedIn && this.index > 1 && <Question question={this.question} onSubmit={this.onQuestionSubmitted.bind(this)}></Question>
      }
        
      </div>
    );
  }

  componentDidMount() {
    this.onQuestionSubmitted();
  }

  onQuestionSubmitted() {    

    axios.get(`${App.URL}${this.index}`)
      .then((q) => {
        if (q.data) {
          this.question = q.data[0];
          this.forceUpdate();
          this.index++;
        }
      })
      .catch((error) => {
        debugger;
        this.forceUpdate();
      });


  }

  logInUser() {
    this.loggedIn = true;
    this.forceUpdate();
  }
}

export default App;
