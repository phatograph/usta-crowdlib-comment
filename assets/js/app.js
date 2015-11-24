let ItemBox = React.createClass({
  loadItemsFromServer() {
    $.ajax({
      url: this.state.url,
      dataType: 'json',
      cache: false,
      beforeSend: function (xhr) {
        xhr.setRequestHeader ("Authorization", `Basic ${btoa("pw:57")}`);
      },
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
      beforeSend: function (xhr) {
        xhr.setRequestHeader ("Authorization", `Basic ${btoa("pw:57")}`);
      },
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
        <div className="panel panel-default">
        <div className="panel-body">
        <a href={link}><h2 className="">{this.props.title}</h2></a>
        {this.props.children}
        </div>
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
        <div className="panel panel-default">
        <div className="panel-heading">
        Add new item
        </div>
        <div className="panel-body">
        <form className="itemForm" onSubmit={this.handleSubmit}>
        <input
        type="text"
        placeholder="Title"
        value={this.state.title}
        onChange={this.handleTitleChange}
        />
        <input type="submit" value="Post" />
        </form>
        </div>
        </div>
        );
  }
});

let ItemInfo = React.createClass({
  loadItemsFromServer(from=0) {
    $.ajax({
      url: `${this.state.url}/${this.props.params.id}?from=${from}&limit=20`,
      dataType: 'json',
      beforeSend: function (xhr) {
        xhr.setRequestHeader ("Authorization", `Basic ${btoa("pw:57")}`);
      },
      cache: false,
      success: data => {
        this.setState((previousState, currentProps) => {
          data.comments = previousState.data.comments.concat(data.comments);
          return {data};
        });
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
      beforeSend: function (xhr) {
        xhr.setRequestHeader ("Authorization", `Basic ${btoa("pw:57")}`);
      },
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
      beforeSend: function (xhr) {
        xhr.setRequestHeader ("Authorization", `Basic ${btoa("pw:57")}`);
      },
      contentType: 'application/json',
      type: 'POST',
      success: data => {
        let comment = this.state.data.comments.find(x => {
          return x.id == data.id
        });

        comment.favourites = data.favourites;
        this.forceUpdate();
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
      beforeSend: function (xhr) {
        xhr.setRequestHeader ("Authorization", `Basic ${btoa("pw:57")}`);
      },
      type: 'DELETE',
      success: data => {
        let comment = this.state.data.comments.find(x => {
          return x.id == data.id
        });

        comment.favourites = data.favourites;
        this.forceUpdate();
      }.bind(this),
      error: (xhr, status, err) => {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },
  loadMoreComments(e) {
    e.preventDefault();
    this.loadItemsFromServer(this.state.data.commentsMore);
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
        <a href="#" onClick={this.loadMoreComments} className={this.state.data.commentsMore > 0 ? '' : 'hidden'}>load more</a>
        <CommentForm onItemSubmit={this.handleItemSubmit} />
        </div>
        );
  }
});

let CommentList = React.createClass({
  render() {
    let comments = this.props.data.map(comment => {
      return (
          <div key={comment.id} className="panel panel-default">
          <div className="panel-body">
          {comment.content}
          </div>
          <div className="panel-footer">
          {comment.user.name} – {comment.date}
          <div className="pull-right">
          <span className="badge">{comment.favourites}</span>
          <a href="#" className="btn btn-default btn-xs" rel={comment.id} onClick={this.props.onFavourite}>Favourite</a>
          <a href="#" className="btn btn-default btn-xs" rel={comment.id} onClick={this.props.onUnFavourite}>Unfavourite</a>
          </div>
          </div>
          </div>
          );
    });

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
        <div className="panel panel-default">
        <div className="panel-heading">
        Add new comment
        </div>
        <div className="panel-body">
        <form className="itemForm" onSubmit={this.handleSubmit}>
        <input
        type="text"
        placeholder="Content"
        value={this.state.content}
        onChange={this.handleContentChange}
        />
        <input type="submit" value="Post" />
        </form>
        </div>
        </div>
        );
  }
});

let UserBox = React.createClass({
  loadItemsFromServer() {
    $.ajax({
      url: `${this.state.url}`,
      dataType: 'json',
      beforeSend: function (xhr) {
        xhr.setRequestHeader ("Authorization", `Basic ${btoa("pw:57")}`);
      },
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
      beforeSend: function (xhr) {
        xhr.setRequestHeader ("Authorization", `Basic ${btoa("pw:57")}`);
      },
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
    let users = this.state.data.map(user => {
      return (
          <div key={user.id}>
          {user.name} {user.surname} |
          <a href="#" rel={user.id} onClick={this.setActive}>make active</a> |
          </div>
          );
    });

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
      beforeSend: function (xhr) {
        xhr.setRequestHeader ("Authorization", `Basic ${btoa("pw:57")}`);
      },
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
      beforeSend: function (xhr) {
        xhr.setRequestHeader ("Authorization", `Basic ${btoa("pw:57")}`);
      },
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
    let list = this.state.data.map(x => {
      let link = `#/items/${x.comment.item.id}`;
      return (
          <div key={x.id} className="panel panel-default">
          <div className="panel-body">
          "{x.comment.content}" on <a href={link}>{x.comment.item.title}</a> by {x.comment.user.name}
          </div>
          </div>
          );
    });
    return (
        <div className="notifications">
        <h3 className="page-header">
        Notifications
        <a href="#" className="pull-right btn btn-default btn-sm" onClick={this.markAsRead}>mark all as read</a>
        </h3>
        {list}
        </div>
        );
  }
});

const App = React.createClass({
  render() {
    return (
        <div>
        <nav className="navbar navbar-default navbar-static-top">
        <div className="container">
        <ul className="nav navbar-nav">
        <li><a href="#/items">Items</a></li>
        <li><a href="#/users">Users</a></li>
        </ul>
        </div>
        </nav>
        <div className="container">
        <div className="row">
        <div className="col-xs-8">
        {this.props.children}
        </div>
        <div className="col-xs-4">
        <Notifications />
        </div>
        </div>
        </div>
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
