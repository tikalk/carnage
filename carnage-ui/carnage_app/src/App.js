import React, { Component } from 'react';
// import Login from './login/login.component';
import Question from './question/question.component';
import axios from 'axios';
import './App.css';

class App extends Component {

  index = 0;

  static URL = 'https://807npuj887.execute-api.us-west-1.amazonaws.com/dev/question/'

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
        <Question question={this.question} onSubmit={this.onQuestionSubmitted}></Question>
      </div>
    );
  }

  componentDidMount() {
    this.onQuestionSubmitted();
  }

  onQuestionSubmitted() {
    axios.get(`${App.URL}${this.index}`)
      .then((function (that) {
        return (q) => {
          debugger;
          that.question = q;
          that.index++;
          that.forceUpdate();
        }
      })(this))
      .catch((function (that) {
        return (error) => {
          
          that.forceUpdate();
        }
      })(this));
    
    
  }
}

export default App;
