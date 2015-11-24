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

let UserBox = React.createClass({
  loadItemsFromServer() {
    $.ajax({
      url: `${this.state.url}`,
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
  setActive(e) {
    e.preventDefault();
    $.ajax({
      url: `${this.state.url}/${e.target.rel}/act`,
      dataType: 'json',
      contentType: 'application/json',
      type: 'PUT',
      success: data => {
        if (data) {
          document.location.reload(true);
        }
      }.bind(this),
      error: (xhr, status, err) => {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },
  getInitialState() {
    return {
      url: 'http://localhost:9998/users',
      data: []
    };
  },
  componentDidMount() {
    this.loadItemsFromServer();
  },
  render() {
    let users = this.state.data.map(user => (
          <div key={user.id}>
          {user.name} {user.surname} |
          <a href="#" rel={user.id} onClick={this.setActive}>make active</a> |
          </div>
          ));
    return (
        <div className="itemInfo">
        <h1>Users</h1>
        {users}
        </div>
        );
  }
});

let Notifications = React.createClass({
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
  markAsRead(e) {
    e.preventDefault();

    $.ajax({
      url: this.state.url,
      dataType: 'json',
      cache: false,
      type: 'PUT',
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
      url: 'http://localhost:9998/users/notifications',
      data: []
    };
  },
  componentDidMount() {
    this.loadItemsFromServer();
    window.addEventListener('hashchange', () => {
      this.loadItemsFromServer();
    })
  },
  render() {
    let list = this.state.data.map(x => (
          <div key={x.id}>
          "{x.comment.content}" on {x.comment.item.title}
          by {x.comment.user.name}
          </div>
          ));
    return (
        <div className="notifications">
        <h4>Notifications</h4>
        <a href="#" onClick={this.markAsRead}>mark all as read</a>
        {list}
        </div>
        );
  }
});

const App = React.createClass({
  render() {
    return (
        <div>
        <a href="#/items"><h2>Items</h2></a>
        <a href="#/users"><h2>Users</h2></a>
        <div>
        {this.props.children}
        </div>
        <Notifications />
        </div>
        )
  }
})

let Route = window.ReactRouter.Route;
let Router = window.ReactRouter.Router;
let IndexRoute = window.ReactRouter.IndexRoute;

ReactDOM.render((
      <Router>
      <Route path="/" component={App}>
      <Route path="items" component={ItemBox} />
      <Route path="items/:id" component={ItemInfo} />
      <Route path="users" component={UserBox} />
      </Route>
      </Router>
      ), document.getElementById('content'));
