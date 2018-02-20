import React, { Component } from 'react';
// import Login from './login/login.component';
import Question from './question/question.component';
import './App.css';

class App extends Component {

  question = {
    question: "what is your name?",
    answers: [
      'Roy',
      'Itay',
      'Keren',
      'Pinhas'
    ]
  }

  render() {
    return (
      <div className="App">
        <Question question={this.question}></Question>
      </div>
    );
  }
}

export default App;
