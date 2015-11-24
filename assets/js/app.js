let ItemBox = React.createClass({
  loadItemsFromServer() {
    $.ajax({
      url: this.state.url,
      dataType: 'json',
      cache: false,
      success: data => {
        this.setState({data: data});
      }.bind(this),
      error: (xhr, status, err) => {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },
  handleItemSubmit(item) {
    $.ajax({
      url: this.state.url,
      dataType: 'json',
      contentType: 'application/json',
      type: 'POST',
      data: JSON.stringify(item),
      success: data => {
        this.loadItemsFromServer();
      }.bind(this),
      error: (xhr, status, err) => {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },
  getInitialState() {
    return {
      pollInterval: 200,
      url: 'http://localhost:9998/items',
      data: []
    };
  },
  componentDidMount() {
    this.loadItemsFromServer();
    // setInterval(this.loadItemsFromServer, this.pollInterval);
  },
  render() {
    return (
        <div className="itemBox">
        <h1>Items</h1>
        <ItemList data={this.state.data} />
        <ItemForm onItemSubmit={this.handleItemSubmit} />
        </div>
        );
  }
});

let ItemList = React.createClass({
  render() {
    let itemNodes = this.props.data.map(item => {
      return (
          <Item title={item.title} id={item.id} key={item.id}>
          {`${item.user.name} ${item.user.surname}`}
          </Item>
          );
    });
    return (
        <div className="itemList">
        {itemNodes}
        </div>
        );
  }
});

let Item = React.createClass({
  render() {
    let link = `#/items/${this.props.id}`;
    return (
        <div className="item">
        <a href={link}><h2 className="">{this.props.title}</h2></a>
        {this.props.children}
        </div>
        );
  }
});

let ItemForm = React.createClass({
  getInitialState() {
    return {title: ''};
  },
  handleTitleChange(e) {
    this.setState({title: e.target.value});
  },
  handleSubmit(e) {
    e.preventDefault();
    let title = this.state.title.trim();
    if (!title) {
      return;
    }
    this.props.onItemSubmit({title: title});
    this.setState({title: ''});
  },
  render() {
    return (
        <form className="itemForm" onSubmit={this.handleSubmit}>
        <input
        type="text"
        placeholder="Title"
        value={this.state.title}
        onChange={this.handleTitleChange}
        />
        <input type="submit" value="Post" />
        </form>
        );
  }
});

let ItemInfo = React.createClass({
  loadItemsFromServer() {
    $.ajax({
      url: `${this.state.url}/${this.props.params.id}`,
      dataType: 'json',
      cache: false,
      success: data => {
        this.setState({data: data});
      }.bind(this),
      error: (xhr, status, err) => {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },
  handleItemSubmit(x) {
    $.ajax({
      url: `${this.state.url}/${this.props.params.id}/reply`,
      dataType: 'json',
      contentType: 'application/json',
      type: 'POST',
      data: JSON.stringify(x),
      success: data => {
        this.loadItemsFromServer();
      }.bind(this),
      error: (xhr, status, err) => {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },
  onFavourite(e) {
    e.preventDefault();

    $.ajax({
      url: `http://localhost:9998/comments/${e.target.rel}/favourite`,
      dataType: 'json',
      contentType: 'application/json',
      type: 'POST',
      success: data => {
        if (data) {
          this.loadItemsFromServer()
        }
      }.bind(this),
      error: (xhr, status, err) => {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },
  onUnFavourite(e) {
    e.preventDefault();

    $.ajax({
      url: `http://localhost:9998/comments/${e.target.rel}/unfavourite`,
      dataType: 'json',
      contentType: 'application/json',
      type: 'DELETE',
      success: data => {
        if (data) {
          this.loadItemsFromServer()
        }
      }.bind(this),
      error: (xhr, status, err) => {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },
  getInitialState() {
    return {
      url: 'http://localhost:9998/items',
      data: {
        item: {},
        comments: []
      }
    };
  },
  componentDidMount() {
    this.loadItemsFromServer();
  },
  render() {
    return (
        <div className="itemInfo">
        <h2>{this.state.data.item.title}</h2>
        <CommentList onFavourite={this.onFavourite} onUnFavourite={this.onUnFavourite} data={this.state.data.comments} />
        <CommentForm onItemSubmit={this.handleItemSubmit} />
        </div>
        );
  }
});

let CommentList = React.createClass({
  render() {
    let comments = this.props.data.map(comment => (
          <div key={comment.id}>
          <div>
          {comment.content} – {comment.user.name} –
          {comment.date} – {comment.favourites} –
          <a href="#" rel={comment.id} onClick={this.props.onFavourite}>fav</a> |
          <a href="#" rel={comment.id} onClick={this.props.onUnFavourite}>unfav</a>
          </div>
          </div>
          ));
    return (
        <div className="commentList">
        {comments}
        </div>
        );
  }
});

let CommentForm = React.createClass({
  getInitialState() {
    return {content: ''};
  },
  handleContentChange(e) {
    this.setState({content: e.target.value});
  },
  handleSubmit(e) {
    e.preventDefault();
    let content = this.state.content.trim();
    if (!content) {
      return;
    }
    this.props.onItemSubmit({content: content});
    this.setState({content: ''});
  },
  render() {
    return (
        <form className="itemForm" onSubmit={this.handleSubmit}>
        <input
        type="text"
        placeholder="Content"
        value={this.state.content}
        onChange={this.handleContentChange}
        />
        <input type="submit" value="Post" />
        </form>
        );
  }
});

const App = React.createClass({
  render() {
    return (
        <div>
        <a href="#/items"><h1>App</h1></a>
        {this.props.children}
        </div>
        )
  }
})

let Route = window.ReactRouter.Route;
let Router = window.ReactRouter.Router;

ReactDOM.render((
      <Router>
      <Route path="/" component={App}>
      <Route path="/items" component={ItemBox} />
      <Route path="items/:id" component={ItemInfo} />
      </Route>
      </Router>
      ), document.getElementById('content'));
